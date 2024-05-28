package hr.fer.oprpp2.util;

public class ValidateUtil {

    public static String validateLoginForm(String nickname, String password) {
        if (nickname == null || nickname.isEmpty()) return "Nickname cant be empty.";
        if (password == null || password.isEmpty()) return "Password cant be empty.";

        return null;
    }

    public static String validateRegisterForm(String firstname, String lastname, String email,
                                              String nickname, String password) {
        if (firstname == null || firstname.isEmpty()) return "First name cant be empty.";
        if (lastname == null || lastname.isEmpty()) return "Last name cant be empty.";
        if (email == null || email.isEmpty()) return "Email cant be empty.";
        if (nickname == null || nickname.isEmpty()) return "Nickname cant be empty.";
        if (password == null || password.isEmpty()) return "Password cant be empty.";

        return null;
    }

    public static String validateNewEntry(String title, String text) {
        if (title == null || title.isEmpty()) return "Title cant be empty.";
        if (text == null || text.isEmpty()) return "Text cant be empty.";

        return null;
    }

    public static String validateComment(String message) {
        if (message == null || message.isEmpty()) return "Message cant be empty.";

        return null;
    }

}
