package com.libraryms.service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    public EmailService(JavaMailSender m){ this.mailSender=m; }

    @Async public void sendWelcomeEmail(String to, String name) {
        send(to,"Welcome to LibraryMS","Dear "+name+",\n\nYour account has been created.\nPlease wait for ID verification approval before borrowing books.\n\nLibraryMS Team");
    }
    @Async public void sendIdVerifiedEmail(String to, String name) {
        send(to,"Your ID has been Verified","Dear "+name+",\n\nYour ID has been verified! You can now borrow books.\n\nLibraryMS Team");
    }
    @Async public void sendBorrowConfirmation(String to, String name, String title, String due) {
        send(to,"Book Borrowed: "+title,"Dear "+name+",\n\nYou borrowed: "+title+"\nDue: "+due+"\n\nLibraryMS Team");
    }
    @Async public void sendOverdueReminder(String to, String name, String title, long days, double fine) {
        send(to,"OVERDUE: "+title,"Dear "+name+",\n\n\""+title+"\" is "+days+" day(s) overdue. Fine: Rs."+fine+"\n\nLibraryMS Team");
    }
    @Async public void sendFineNotification(String to, String name, String title, double fine) {
        send(to,"Fine Charged - "+title,"Dear "+name+",\n\nFine of Rs."+fine+" charged for \""+title+"\".\n\nLibraryMS Team");
    }
    private void send(String to, String subject, String body) {
        try { SimpleMailMessage m=new SimpleMailMessage(); m.setTo(to); m.setSubject(subject); m.setText(body); mailSender.send(m); }
        catch(Exception e){ System.err.println("Email failed: "+e.getMessage()); }
    }
}
