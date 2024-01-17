# My notes
1/10 - Java Fundamentals
- **File organization**
    - code in .java files --> .class files
    - one file per class and the names have to match (X.java --> X.class)
- Run a java
    - javac (path to )
        - java -cp bin/ X arg1 arg2 ...
        - java (the java virtual machine runs your program)
        - 'cp' (class path (directory containing the .class files))
        - 'X' (name of the class with the main method)
        - "argN" (command line arguments passed to main method)
- Documentation
    - strings are immutable, concatenation always creates a new string
    - String s = "Hello";
    - String s = new String("Hello");
    - concatenation
        - String s1 = "Hello";
        - String s2 = "BYU";
        - String s3 = s1 + " " + s2;
        - StringBuilder
            - to avoid creating temporary strings

##### Might be using strings a bit in the first assignment
Here is the documentation: [String Javadocs](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/String.html)

## Chess Phase 0 in class notes
**2D array**  would be good to make as the chess board.
#### how to structure the classes
- ChessPiece class
  - type: PieceType
  - color: TeamColor
  - constructor(color: TeamColor, type:PieceType)
  - etc.
- In picture, each box is a separate class.
- use static methods.
