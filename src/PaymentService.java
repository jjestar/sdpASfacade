import java.util.HashMap;
import java.util.Map;

public class PaymentService {
    private final Map<String, Integer> balances = new HashMap<>();

    public PaymentService() {
        balances.put("s1", 5000);
        balances.put("s2", 300);
    }

    public int getBalance(String studentId) {
        Integer balance = balances.get(studentId);
        if (balance == null) {
            throw new IllegalArgumentException("Unknown student: " + studentId);
        }
        return balance;
    }

    public void charge(String studentId, int amount) {
        checkAmount(amount);
        int balance = getBalance(studentId);
        if (balance < amount) {
            throw new IllegalStateException("Not enough money in the account.");
        }
        balances.put(studentId, balance - amount);
    }

    public void refund(String studentId, int amount) {
        checkAmount(amount);
        int balance = getBalance(studentId);
        balances.put(studentId, Math.addExact(balance, amount));
    }

    private void checkAmount(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive.");
        }
    }
}
