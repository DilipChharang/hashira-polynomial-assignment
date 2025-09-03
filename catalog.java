import org.json.JSONObject;
import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
public class catalog {

    // Expand polynomial coefficients (multiply (x - root) terms)
    public static List<BigInteger> constructPolynomial(List<BigInteger> roots) {
        // Start with P(x) = 1
        List<BigInteger> coeffs = new ArrayList<>();
        coeffs.add(BigInteger.ONE);

        for (BigInteger r : roots) {
            List<BigInteger> newCoeffs = new ArrayList<>();
            newCoeffs.add(coeffs.get(0).negate().multiply(r)); // constant term

            // middle terms
            for (int i = 1; i < coeffs.size(); i++) {
                BigInteger val = coeffs.get(i).negate().multiply(r).add(coeffs.get(i - 1));
                newCoeffs.add(val);
            }
            newCoeffs.add(coeffs.get(coeffs.size() - 1)); // highest power term stays 1

            coeffs = newCoeffs;
        }

        // Reverse to standard form a0 + a1x + ... + anx^n
        Collections.reverse(coeffs);
        return coeffs;
    }

    public static void main(String[] args) throws Exception {
           // Read JSON file
        String jsonInput = new String(Files.readAllBytes(Paths.get("input.json")));

        // Parse JSON
        JSONObject obj = new JSONObject(jsonInput);
        JSONObject keys = obj.getJSONObject("keys");

        int n = keys.getInt("n");
        int k = keys.getInt("k");

        System.out.println("n = " + n + ", k = " + k);
        List<BigInteger> roots = new ArrayList<>();
        int count = 0;
        for (String key : obj.keySet()) {
            if (key.equals("keys")) continue;
            if (count == k) break;
            JSONObject rootObj = obj.getJSONObject(key);
            int base = rootObj.getInt("base");
            String value = rootObj.getString("value");

            BigInteger root = new BigInteger(value, base);
            roots.add(root);
            count++;
        }

        // Construct polynomial
        List<BigInteger> coeffs = constructPolynomial(roots);

        // Print result
        System.out.println("Polynomial Coefficients (highest degree first):");
        for (BigInteger c : coeffs) {
            System.out.print(c + " ");
        }
        System.out.println();
    }
}
