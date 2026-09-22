### Definition:

Java does not allocate a 2D array as a single, contiguous, flat block of memory. Instead, the "main" array only holds memory references pointing to other 1D arrays (the rows).

1. Jagged Arrays (Uneven Rows):
   - Because every row is an independent 1D array, they do not have to be the same length. You can create jagged or ragged arrays where each row has a different number of columns.
   ```java
   // Create the main array with 3 rows, but leave columns undefined
   int[][] jaggedArray = new int[3][];
   // Initialize each row to a different length
   jaggedArray[0] = new int[2]; // Row 0 has 2 columns
   jaggedArray[1] = new int[5]; // Row 1 has 5 columns
   jaggedArray[2] = new int[1]; // Row 2 has 1 column
   ```
2. Independent Length Checking:
   - Because there is no fixed grid, you must check the length of the specific row you are working with rather than assuming a universal column count.
   ```java
   myArray.length      // returns number of rows
   myArray[i].length   // returns number of columns in row i
   ```

If you initialize a 2D array all at once (e.g., `new int[3][4]`), Java simply handles creating the three 4-element arrays for you behind the scenes, but under the hood, it remains an array of independent arrays.
