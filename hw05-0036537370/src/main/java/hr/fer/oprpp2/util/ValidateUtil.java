package hr.fer.oprpp2.util;

/**
 * Util class for validation the forms' validation.
 */
public class ValidateUtil {

    /**
     * Method for login form validation.
     * @param nickname Non-empty nickname
     * @param password Non-empty password
     * @return Boolean representing if the form is valid
     */
    public static String validateLoginForm(String nickname, String password) {
        if (nickname == null || nickname.isEmpty()) return "Nickname cant be empty.";
        if (password == null || password.isEmpty()) return "Password cant be empty.";

        return null;
    }

    /**
     * Method for register form validation.
     * @param firstname Non-empty first name
     * @param lastname Non-empty last name
     * @param email Non-empty email
     * @param nickname Non-empty nickname
     * @param password Non-empty password
     * @return Boolean representing if the form is valid
     */
    public static String validateRegisterForm(String firstname, String lastname, String email,
                                              String nickname, String password) {
        if (firstname == null || firstname.isEmpty()) return "First name cant be empty.";
        if (lastname == null || lastname.isEmpty()) return "Last name cant be empty.";
        if (email == null || email.isEmpty()) return "Email cant be empty.";
        if (nickname == null || nickname.isEmpty()) return "Nickname cant be empty.";
        if (password == null || password.isEmpty()) return "Password cant be empty.";

        return null;
    }

    /**
     * Method for new entry form validation.
     * @param title Non-empty title
     * @param text Non-empty text
     * @return Boolean representing if the form is valid
     */
    public static String validateNewEntry(String title, String text) {
        if (title == null || title.isEmpty()) return "Title cant be empty.";
        if (text == null || text.isEmpty()) return "Text cant be empty.";

        return null;
    }

    /**
     * Method for comment form validation.
     * @param message Non-empty message
     * @return Boolean representing if the form is valid
     */
    public static String validateComment(String message) {
        if (message == null || message.isEmpty()) return "Message cant be empty.";

        return null;
    }

}
