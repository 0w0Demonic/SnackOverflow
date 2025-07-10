package main;

/**
 * Common utils.
 */
public final class Util
{
    public static boolean isNumber(String str) {
        if (!str.chars().allMatch(Character::isDigit)) {
            return false;
        }
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
