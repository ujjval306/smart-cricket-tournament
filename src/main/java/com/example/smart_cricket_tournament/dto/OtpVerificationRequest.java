package com.example.smart_cricket_tournament.dto;

public record OtpVerificationRequest(
        String email, String otp
) {
}
