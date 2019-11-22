import java.util.Arrays;
import java.util.IllegalFormatCodePointException;


public class Matrix {

    /**
     * @param mat array amb la que farem feina
     * @return double suma de la diagonal de la matriu
     */
    static double trace(double[][] mat) {
        /*
        Agafarem, amb el bucle for, els elements de la
        diagonal de la matriu i els sumarem a la vaiable result.
         */

        if (!matrixExists(mat) || !squareMatrix(mat)){
            return Double.NaN;
        }

        double result = 0;
        for (int i = 0; i < mat.length; i++) {
            result = result + mat[i][i];
        }
        return result;
    }

    /**
     * @param mat1
     * @param mat2
     * @return
     */
    static double[][] add(double[][] mat1, double[][] mat2) {
        /**
        Crearem un array bidimensional (result[][]) on guardar la
        suma dels dos arrays importats (mat1 i mat2).
        Amb els fors recorrerem les posicions de mat1
        i mat2 i les guardarem a result.
         */

        if (!matrixExists(mat1) || !matrixExists(mat2) || !testSameDimension(mat1, mat2)){
            return null;
        }

        double result[][] = new double[mat1.length][mat1.length];

        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result.length; j++) {
                result[i][j] = mat1[i][j] + mat2[i][j];
            }
        }

        return result;
    }

    /**
     * @param mat1
     * @param mat2
     * @return
     */
    static double[][] mult(double[][] mat1, double[][] mat2) {

        if (!matrixExists(mat1) || !matrixExists(mat2) || mat1[0].length != mat2.length){
            return null;
        }

        double result[][] = new double[mat1.length][mat2[0].length];

        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                for (int k = 0; k < result.length; k++) {
                    result[i][j] = result[i][j] + mat1[i][k] * mat2[k][j];
                }
            }
        }

        return result;

    }

    /**
     * @param mat
     * @param p
     * @return
     */
    static double[][] power(double[][] mat, int p) {

        if (!matrixExists(mat)){
            return null;
        }

        double result[][] = new double[mat.length][mat[0].length];

        if (p == 0){
           result = matriuUnitaria(result);
           return result;
        }

        // Bucle per copiar la matriu "mat" dins la matriu amb la que farem feina més endavant.
        for (int i = 0; i < mat.length; i++) {
            System.arraycopy(mat[i], 0, result[i], 0, mat[0].length);
        }

        for (int i = 1; i < p; i++) {
            result = mult(result, mat);
        }

        return result;
    }

    /**
     * @param mat1
     * @param mat2
     * @return
     */
    static double[][] div(double[][] mat1, double[][] mat2) {

        if ( !matrixExists(mat1) || !matrixExists(mat2) || !squareMatrix(mat1) || !squareMatrix(mat2)){
            return null;
        } else if ( determinant(mat2) == 0 ){
            return null;
        }

        double[][] result = mult(mat1, invert(mat2));

        return result;
    }

    /**
     * @param mat
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    static double[][] submatrix(double[][] mat, int x1, int y1, int x2, int y2) {

        if ( !matrixExists(mat) ){
            return null;
        } else if(y1 < 0 || y1 > mat.length - 1 || y2 < y1 || y2 > mat.length - 1){
            return null;
        } else if(x1 < 0 || x1 > mat[0].length - 1 || x2 < x1 || x2 > mat[0].length - 1){
            return null;
        }


        int files = 0;
        int columnes = 0;

        // averiguar les files
        for ( int i = 0; i < mat.length; i++ ) {
            if ( i >= y1 && i <= y2 ){
                files++;
            }
        }

        // averiguar les columnes
        for (int i = 0; i < mat[0].length; i++) {
            if (i >= x1 && i <= x2){
                columnes++;
            }
        }

        double[][] result = new double[files][columnes];

        int a = 0, b = 0;
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                if (i >= y1 && i <= y2 && j >= x1 && j <= x2){
                    result[a][b] = mat[i][j];
                    b++;
                    if (b >= result[0].length){
                        b = 0;
                        a++;
                    }
                }
            }
        }

        return result;
    }

    /**
     * @param mat
     * @param n
     * @return
     */
    static double[][] mult(double[][] mat, double n) {
        /**
        En aquesta funció multiplicarem el valor de la posició
        de la matriu entrar per l'escalar importat.
        */

        if ( !matrixExists(mat) || Double.isNaN(n) ){
            return null;
        }

        double result[][] = new double[mat.length][mat[0].length];

        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                result[i][j] = n * mat[i][j];
            }
        }

        return result;
    }

    /**
     * @param mat
     * @return
     */
    static double[][] invert(double[][] mat) {

        if ( !matrixExists(mat) || !squareMatrix(mat)){
            return null;
        }

        double[][] result = new double[mat.length][mat[0].length];
        double[][] aux = new double[mat.length][mat[0].length];

        double determinant = determinant(mat);

        if (determinant == 0){
            return null;
        }

        // copiam la matriu importada a una local per manipularla
        System.arraycopy(mat, 0, aux, 0, mat.length);

        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                result[i][j] = (Math.pow(-1, i + j)) * determinant(getMinor(aux, i, j));
            }
        }

        result = transpose(result);

        result = mult(result, 1 / determinant);

        return result;
    }

    /**
     * @param mat
     * @param x
     * @param y
     * @return
     */
    static double[][] getMinor(double[][] mat, int x, int y) {
        /**
        Aquesta funció retorna una matriu sense la fila i
        la columna del valor amb coordenades x i y.
        */

        if ( !matrixExists(mat) ){
            return null;
        } else if ( x < 0 || x > mat.length - 1 ){
            return null;
        } else if ( y < 0 || y > mat[0].length - 1 ){
            return null;
        }

        double result[][] = new double[mat.length - 1][mat[0].length - 1];

        int a = 0, b = 0;
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                if (x != i && y != j) {
                    result[a][b] = mat[i][j];
                    b++;
                    if (b == result[0].length) {
                        b = 0;
                        a++;
                    }
                }
            }
        }

        return result;
    }

    /**
     * @param mat
     * @return
     */
    static double determinant(double[][] mat) {

        if ( !matrixExists(mat) || !squareMatrix(mat) ){
            return Double.NaN;
        }

        double result = 0;

        // Si la matriu és de 1x1
        if (mat.length == 1) {
            result = mat[0][0];
            return result;
        }

        // Si la matriu és de 2x2
        if (mat.length == 2) {
            result = (mat[0][0] * mat[1][1]) - (mat[0][1] * mat[1][0]);
            return result;
        }

        //Si la matriu és de nxn on n>=3
        for (int i = 0; i < mat[0].length; i++) {
            double[][] aux = getMinor(mat, 0, i);
            result = result + (mat[0][i] * Math.pow(-1, i)) * determinant(aux);
        }

        return result;
    }

    /**
     * @param mat
     * @return result
     */
    static double[][] transpose(double[][] mat) {
        /**
        Crearem un array bidimensional (result[][]) on guardarem els valors de les posicions de l'array importat
        però amb els indexs girats. Per exemple: result[2][3] = mat[3][2].
         */

        if (!matrixExists(mat)){
            return null;
        }

        double result[][] = new double[mat[0].length][mat.length];

        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                result[i][j] = mat[j][i];
            }
        }

        return result;
    }

    /**
     * @param mat
     * @return
     */
    static boolean isOrtho(double[][] mat) {

        if (!matrixExists(mat) || determinant(mat)  == 0 || !squareMatrix(mat)){
            return false;
        }

        double[][] trans = transpose(mat);
        double[][] inv = invert(mat);

        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat.length; j++) {
                double a = Math.abs(trans[i][j]);
                double b = Math.abs(inv[i][j]);
                if (a - b > 0.0001){
                    return false;
                }
            }
        } return true;

    }

    /**
     * @param mat
     * @return
     */
    static double[] cramer(double[][] mat) {

        if ( !matrixExists(mat) ){
            return null;
        }

        double[][] matriuAmpliada = mat;
        double[][] matriu = new double[mat.length][mat[0].length - 1];
        double[] result = new double[matriu.length];

        // copiarem la matriu mat NO ampliada
        for (int i = 0; i < matriu.length; i++) {
            for (int j = 0; j < matriu[0].length; j++) {
                matriu[i][j] = mat[i][j];
            }
        }

        if (!squareMatrix(matriu)){
            return null;
        }

        /**
        per poder utilitzar el mètode de Cramer, el determinant
        la matriu ha de ser diferent de 0.
         */
        double determinantMatriu = determinant(matriu);

        if (determinantMatriu == 0){
            return null;
        }

        for (int i = 0; i < result.length; i++) {   // donarà x, y, z...
            // sustituirem la columna desitjada
            for (int j = 0; j < matriu.length; j++) {
                matriu[j][i] = matriuAmpliada[j][matriuAmpliada[0].length - 1];
            }

            result[i] = determinant(matriu) / determinantMatriu;

            // resustituirem la columna modificada per la inicial
            for (int j = 0; j < matriu.length; j++) {
                matriu[j][i] = matriuAmpliada[j][i];
            }
        }
        return result;
    }

    private static double[][] matriuUnitaria(double[][] mat){

        double[][] matriuUnitaria = new double[mat.length][mat[0].length];

        for (int i = 0; i < matriuUnitaria.length; i++) {
            for (int j = 0; j < matriuUnitaria[0].length; j++) {
                if (i == j){
                    matriuUnitaria[i][j] = 1;
                } else{
                    matriuUnitaria[i][j] = 0;
                }
            }
        }

        return matriuUnitaria;
    }

    private static boolean testSameDimension(double mat1[][], double mat2[][]){

        // comprovar que tenguin la mateixa dimensió
        if (mat1[0].length != mat2[0].length || mat1.length != mat2.length) {
            return false;
        }

        return true;
    }

    private static boolean squareMatrix(double mat[][]){

        if (mat.length != mat[0].length){
            return false;
        } return true;

    }

    private static boolean matrixExists(double mat[][]){

        if (mat.length == Double.NaN){
            return false;
        }

        if (mat.length == 0 || mat[0].length == 0){
            return false;
        }

        for (int i = 0; i < mat.length - 1; i++) {
            if (mat[i].length != mat[i + 1].length){
                return false;
            }
        }

        return true;

    }

    // Funció que mostra una matriu per pantalla
    static void printMat(double[][] mat) {
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                System.out.printf("%06.2f ", mat[i][j]);
            }
            System.out.println();
        }
    }
}
