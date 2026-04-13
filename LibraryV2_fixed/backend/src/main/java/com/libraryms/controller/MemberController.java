package com.libraryms.controller;

import com.libraryms.model.Member;
import com.libraryms.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@CrossOrigin(origins = "*")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService ms) {
        this.memberService = ms;
    }

    // ── All members (admin / librarian) ──────────────────────────
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    public ResponseEntity<List<Member>> getAll() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    // ── Single member ─────────────────────────────────────────────
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    public ResponseEntity<Member> getById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }

    // ── Update profile ────────────────────────────────────────────
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    public ResponseEntity<Member> update(@PathVariable Long id, @Valid @RequestBody Member m) {
        return ResponseEntity.ok(memberService.updateMember(id, m));
    }

    // ── Toggle active / inactive ──────────────────────────────────
    @PatchMapping("/{id}/toggle-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Member> toggleStatus(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.toggleStatus(id));
    }

    // ── Delete member ─────────────────────────────────────────────
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    // ── Change password ───────────────────────────────────────────
    @PatchMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        memberService.changePassword(id, oldPassword, newPassword);
        return ResponseEntity.ok().build();
    }

    // ── ID verification: list pending ─────────────────────────────
    @GetMapping("/pending-verification")
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    public ResponseEntity<List<Member>> pendingVerification() {
        return ResponseEntity.ok(memberService.getPendingVerification());
    }

    // ── ID verification: approve ──────────────────────────────────
    @PatchMapping("/{id}/approve-id")
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    public ResponseEntity<Member> approveId(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.approveId(id));
    }

    // ── ID verification: reject ───────────────────────────────────
    @PatchMapping("/{id}/reject-id")
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    public ResponseEntity<Member> rejectId(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.rejectId(id));
    }

    // ── Members by role ───────────────────────────────────────────
    @GetMapping("/by-role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Member>> byRole(@PathVariable Member.Role role) {
        return ResponseEntity.ok(memberService.getMembersByRole(role));
    }
}
