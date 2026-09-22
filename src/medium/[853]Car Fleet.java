/*
 * @lc app=leetcode id=853 lang=java
 *
 * [853] Car Fleet
 */

// @lc code=start

import java.util.ArrayDeque;

class Solution {
    public int carFleet(int target, int[] position, int[] speed) {
        int n = position.length;
        if (n == 0)
            return 0;
        double[][] cars = new double[n][2];
        for (int i = 0; i < n; i++) {
            cars[i][0] = position[i];
            cars[i][1] = (double) (target - position[i]) / speed[i];
        }
        Arrays.sort(cars, (a, b) -> Double.compare(b[0], a[0]));

        Deque<Double> stack = new ArrayDeque<>();
        for (double[] car : cars) {
            stack.push(car[1]);
            if (stack.size() >= 2) {
                double timeOfNextCar = stack.pop();
                double timeOfPreviousCar = stack.peek();
                if (timeOfNextCar > timeOfPreviousCar) {
                    stack.push(timeOfNextCar);
                }
            }
        }
        return stack.size();

    }
}
// @lc code=end
