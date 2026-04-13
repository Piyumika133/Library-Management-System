package com.libraryms.controller;
import com.libraryms.dto.*;
import com.libraryms.model.Member;
import com.libraryms.security.JwtUtils;
import com.libraryms.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException; import java.nio.file.*; import java.util.UUID;

@RestController @RequestMapping("/api/auth") @CrossOrigin(origins="*")
public class AuthController {
    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;
    private final MemberService memberService;
    @Value("${app.upload.dir:uploads/}") private String uploadDir;

    public AuthController(AuthenticationManager am, JwtUtils ju, MemberService ms){
        this.authManager=am; this.jwtUtils=ju; this.memberService=ms;
    }

    // ── Login ──────────────────────────────────────────────
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req){
        try {
            Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(auth);
            String token = jwtUtils.generateJwtToken(auth);
            Member m = memberService.getMemberByEmail(req.getEmail());
            return ResponseEntity.ok(toResponse(token, m));
        } catch (BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorMsg("Invalid email or password"));
        } catch (LockedException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorMsg("Account is deactivated. Contact admin."));
        }
    }

    // ── Register ───────────────────────────────────────────
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req){
        try {
            Member m = Member.builder()
                    .name(req.getName()).email(req.getEmail()).password(req.getPassword())
                    .phone(req.getPhone()).address(req.getAddress())
                    .memberIdNumber(req.getMemberIdNumber())
                    .role(Member.Role.USER).active(true).idVerified(false).build();
            Member saved = memberService.registerMember(m);
            Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
            String token = jwtUtils.generateJwtToken(auth);
            return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(token, saved));
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(new ErrorMsg(e.getMessage()));
        }
    }

    // ── Upload ID Photos (front + back) ───────────────────
    @PostMapping("/upload-id")
    public ResponseEntity<?> uploadId(
            @RequestParam("memberId") Long memberId,
            @RequestParam("front") MultipartFile front,
            @RequestParam("back") MultipartFile back) {
        try {
            Path dir = Paths.get(uploadDir + "ids/"); Files.createDirectories(dir);
            String frontName = UUID.randomUUID() + "_front_" + front.getOriginalFilename();
            String backName  = UUID.randomUUID() + "_back_"  + back.getOriginalFilename();
            Path fp = dir.resolve(frontName); front.transferTo(fp.toFile());
            Path bp = dir.resolve(backName);  back.transferTo(bp.toFile());
            Member updated = memberService.verifyId(memberId, fp.toString(), bp.toString());
            return ResponseEntity.ok(toResponse(null, updated));
        } catch (IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMsg("Upload failed: "+e.getMessage()));
        }
    }

    // ── Logout (client clears token; server returns OK) ───
    @PostMapping("/logout")
    public ResponseEntity<?> logout(){
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new ErrorMsg("Logged out successfully"));
    }

    // ── Get current user profile ──────────────────────────
    @GetMapping("/me")
    public ResponseEntity<?> getMe(Authentication auth){
        if(auth==null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorMsg("Not logged in"));
        Member m = memberService.getMemberByEmail(auth.getName());
        return ResponseEntity.ok(toResponse(null, m));
    }

    private AuthResponse toResponse(String token, Member m){
        return new AuthResponse(token, m.getId(), m.getName(), m.getEmail(),
                m.getRole().name(), m.isIdVerified(), m.getMemberIdNumber(),
                m.getNicFrontPath(), m.getNicBackPath());
    }

    record ErrorMsg(String message){}
}
