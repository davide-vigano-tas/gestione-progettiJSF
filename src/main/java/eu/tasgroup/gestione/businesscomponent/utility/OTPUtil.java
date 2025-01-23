package eu.tasgroup.gestione.businesscomponent.utility;

import java.security.SecureRandom;

public class OTPUtil {
	private static final SecureRandom secureRandom = new SecureRandom();
    private static final int OTP_LENGTH = 6;

    public static String generateOTP() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(secureRandom.nextInt(10)); // Genera un numero tra 0 e 9
        }
        return otp.toString();
    }
}
