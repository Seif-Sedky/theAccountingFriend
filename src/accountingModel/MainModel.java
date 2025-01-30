package accountingModel;

import java.util.Set;

public class MainModel {
	public static String levenshteinDistance(String target, Set<String> keys) {
		int minDistance = Integer.MAX_VALUE;
		String closestMatch = null;

		for (String key : keys) {
			int distance = calculateLevenshteinDistance(target, key);

			if (distance < minDistance) {
				minDistance = distance;
				closestMatch = key;
			}
		}

		// Return the closest match if the minimum distance is 2 or less
		return minDistance <= 2 ? closestMatch : null;
	}

	// Helper method to calculate Levenshtein Distance between two strings
	public static int calculateLevenshteinDistance(String s1, String s2) {
		int m = s1.length();
		int n = s2.length();

		int[][] dp = new int[m + 1][n + 1];

		// Initialize dp table
		for (int i = 0; i <= m; i++) {
			dp[i][0] = i;
		}
		for (int j = 0; j <= n; j++) {
			dp[0][j] = j;
		}

		// Fill the table using dynamic programming
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {
				if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
					dp[i][j] = dp[i - 1][j - 1];
				} else {
					dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], // Substitution
							Math.min(dp[i - 1][j], // Deletion
									dp[i][j - 1])); // Insertion
				}
			}
		}

		return dp[m][n];
	}
}
