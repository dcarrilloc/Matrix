
public class Matrix {

    /**
     * Agafarem, amb el bucle for, els elements de la
     * diagonal de la matriu i els sumarem a la variable result.
     *
     * @param mat array bidimensional de la matriu
     * @return diagonal de la matriu
     */
    static double trace(double[][] mat) {

        if (!matrixExists(mat) || !squareMatrix(mat)) {
            return Double.NaN;
        }

        double result = 0;

        for (int i = 0; i < mat.length; i++) {
            result = result + mat[i][i];
        }
        return result;
    }

    /**
     * Crearem un array bidimensional (result[][]) on guardar la
     * suma de les matrius (mat1 i mat2).
     * Amb els fors recorrerem les posicions de mat1
     * i mat2 i les guardarem a result.
     *
     * @param mat1 matriu 1
     * @param mat2  matriu 2
     * @return matriu resultant de la suma
     */
    static double[][] add(double[][] mat1, double[][] mat2) {
        /*

         */

        if (!matrixExists(mat1) || !matrixExists(mat2) || !testSameDimension(mat1, mat2)) {
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
     * Aquesta funció realitza el producte de dos matrius
     *
     * @param mat1 matriu 1
     * @param mat2 matriu 2
     * @return matriu resultant de fer el producte
     */
    static double[][] mult(double[][] mat1, double[][] mat2) {

        // comprovació de que les matrius existeixen i de que es pot fer la multiplicació
        if (!matrixExists(mat1) || !matrixExists(mat2) || mat1[0].length != mat2.length) {
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
     * Aquesta funció realitza la potència d'una matriu.
     *
     * @param mat matriu que elevarem.
     * @param p exponent de la potència.
     * @return matriu resultant de realitzar la potència.
     */
    static double[][] power(double[][] mat, int p) {

        if (!matrixExists(mat)) {
            return null;
        }

        double result[][] = new double[mat.length][mat[0].length];

        if (p == 0) {
            result = matriuUnitaria(result);
            return result;
        }

        // Bucle per copiar la matriu "mat" dins la matriu result amb la que farem feina més endavant
        for (int i = 0; i < mat.length; i++) {
            System.arraycopy(mat[i], 0, result[i], 0, mat[0].length);
        }

        for (int i = 1; i < p; i++) {
            result = mult(result, mat);
        }

        return result;
    }

    /**
     * Aquesta funció realitza la divisió entre dues matrius.
     * Com que matemàticament no es pot fer la divisió de dues matrius
     * podem multiplicar la primera matriu per la inversa de la segona.
     *
     * @param mat1 matriu 1
     * @param mat2 matriu 2
     * @return el resultat de fer la divisió de les dues matrius
     */
    static double[][] div(double[][] mat1, double[][] mat2) {

        if (!matrixExists(mat1) || !matrixExists(mat2) || !squareMatrix(mat1) || !squareMatrix(mat2)) {
            return null;
        } else if (determinant(mat2) == 0) {
            return null;
        }

        double[][] result = mult(mat1, invert(mat2));

        return result;
    }

    /**
     * A través de les coordenades (x1,y1), (x2,y2) podem extreure una submatriu d'una matriu més gran.
     * Agafarem tots els elements de la matriu original que estiguin dintre del requadre imaginari
     * creat per les coordenades i els afegirem a la matriu resultant.
     *
     * @param mat matriu
     * @param x1 primer element de la coordenada 1
     * @param y1 segon element de la coordenada 1
     * @param x2 primer element de la coordenada 2
     * @param y2 segon element de la coordenada 2
     * @return matriu resultant de calcular la submatriu de la matriu principal
     */
    static double[][] submatrix(double[][] mat, int x1, int y1, int x2, int y2) {

        if (!matrixExists(mat)) {
            return null;
        } else if (y1 < 0 || y1 > mat.length - 1 || y2 < y1 || y2 > mat.length - 1) {
            return null;
        } else if (x1 < 0 || x1 > mat[0].length - 1 || x2 < x1 || x2 > mat[0].length - 1) {
            return null;
        }


        int files = 0;
        int columnes = 0;

        // averiguar les files
        for (int i = 0; i < mat.length; i++) {
            if (i >= y1 && i <= y2) {
                files++;
            }
        }

        // averiguar les columnes
        for (int i = 0; i < mat[0].length; i++) {
            if (i >= x1 && i <= x2) {
                columnes++;
            }
        }

        double[][] result = new double[files][columnes];

        int a = 0, b = 0;
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                if (i >= y1 && i <= y2 && j >= x1 && j <= x2) {
                    result[a][b] = mat[i][j];
                    b++;
                    if (b >= result[0].length) {
                        b = 0;
                        a++;
                    }
                }
            }
        }

        return result;
    }

    /**
     * En aquesta funció multiplicarem el valor de la posició
     * de la matriu per un escalar.
     *
     * @param mat matriu
     * @param n nombre escalar
     * @return matriu resultant de multiplicar una matriu per un escalar.
     */
    static double[][] mult(double[][] mat, double n) {

        if (!matrixExists(mat) || Double.isNaN(n)) {
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
     * Aquesta funció retorna l'inversa d'una matriu.
     * @param mat matriu
     * @return matriu invertida
     */
    static double[][] invert(double[][] mat) {

        if (!matrixExists(mat) || !squareMatrix(mat)) {
            return null;
        }

        double[][] result = new double[mat.length][mat[0].length];
        double[][] aux = new double[mat.length][mat[0].length];

        double determinant = determinant(mat);

        if (determinant == 0) {
            return null;
        }

        // copiam la matriu importada a una local per manipularla
        System.arraycopy(mat, 0, aux, 0, mat.length);

        // sustituim cada element de la matriu per el determinant del seu adjunt.
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
     * Aquesta funció retorna una matriu sense la fila i
     * la columna del valor amb coordenades x i y. És a dir, el seu adjunt.
     * @param mat matriu
     * @param x coordenada x
     * @param y coordenada y
     * @return matriu adjunta
     */
    static double[][] getMinor(double[][] mat, int x, int y) {

        if (!matrixExists(mat)) {
            return null;
        } else if (x < 0 || x > mat.length - 1) {
            return null;
        } else if (y < 0 || y > mat[0].length - 1) {
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
     * Aquesta funció calcula el determinant d'una funció. en aquest cas emprarem el métode
     * de la recursió per a calcular determinants de dimensió qualsevol.
     * @param mat matriu
     * @return deterrminant de la matriu
     */
    static double determinant(double[][] mat) {

        if (!matrixExists(mat) || !squareMatrix(mat)) {
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
     * Aquesta funció calculla transposada d'una funció. Crearem un array bidimensional (result[][])
     * on guardarem els valors de les posicions de la matriu però amb els indexs girats.
     * Per exemple: result[2][3] = mat[3][2].
     *
     * @param mat matriu
     * @return matriu transposada
     */
    static double[][] transpose(double[][] mat) {

        if (!matrixExists(mat)) {
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
     * Aquesta funció retorna un boolean (true / false) segons si la matriu es considera ortogonal.
     * Una matriu és ortogonal si la seva transposada és igual a la seva inversa.
     *
     * @param mat matriu
     * @return true si és ortogonal / false si NO és ortogonal
     */
    static boolean isOrtho(double[][] mat) {

        if (!matrixExists(mat) || determinant(mat) == 0 || !squareMatrix(mat)) {
            return false;
        }

        double[][] trans = transpose(mat);
        double[][] inv = invert(mat);

        /*
         anam comparant element per element de les dues matrius i els restam per saber si
         el valor és el mateix. Decidim l'index d'error, en aquest cas 0.0001, és un delta
         bastant precís.
         */

        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat.length; j++) {
                double a = Math.abs(trans[i][j]);
                double b = Math.abs(inv[i][j]);
                if (a - b > 0.0001) {
                    return false;
                }
            }
        }
        return true;

    }

    /**
     * En aquesta funció implementarem la Regla de Cramer per a la resolució de sistemes d'equacions.
     *
     * @param mat matriu
     * @return array amb els valors de les incògnites [x,y,z,...]
     */
    static double[] cramer(double[][] mat) {

        if (!matrixExists(mat)) {
            return null;
        }

        double[][] matriuAmpliada = mat;
        double[][] matriu = new double[mat.length][mat[0].length - 1];
        double[] result = new double[matriu.length];

        // copiarem la matriu mat NO ampliada
        matriu = submatrix(mat,0,0,matriuAmpliada.length - 1,matriuAmpliada[0].length - 2);

        if (!squareMatrix(matriu)) {
            return null;
        }

        /*
         per poder utilitzar el mètode de Cramer, el determinant
         la matriu ha de ser diferent de 0.
         */
        double determinantMatriu = determinant(matriu);

        if (determinantMatriu == 0) {
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

    /**
     * Aquesta funció retorna la matriu unitària
     *
     * @param mat matriu necesitada per saber la dimensió que tendrà la matriu unitària
     * @return matriu unitària
     */
    private static double[][] matriuUnitaria(double[][] mat) {

        double[][] matriuUnitaria = new double[mat.length][mat[0].length];

        for (int i = 0; i < matriuUnitaria.length; i++) {
            for (int j = 0; j < matriuUnitaria[0].length; j++) {
                if (i == j) {
                    matriuUnitaria[i][j] = 1;
                } else {
                    matriuUnitaria[i][j] = 0;
                }
            }
        }

        return matriuUnitaria;
    }

    /**
     * Aquesta funció retorna un boolean segons si les dues matrius tenen la mateixa dimensió
     *
     * @param mat1 matriu 1
     * @param mat2 matriu 2
     * @return true si tenen la mateixa dimensió / false si no
     */
    private static boolean testSameDimension(double mat1[][], double mat2[][]) {

        if (mat1[0].length != mat2[0].length || mat1.length != mat2.length) {
            return false;
        }

        return true;
    }

    /**
     * Aquesta funció ens diu en forma de boolean si una matriu és quadrada. És a dir, si té
     * el mateix nombre de files que de columnes.
     *
     * @param mat matriu
     * @return true si es quadrada / false si no
     */
    private static boolean squareMatrix(double mat[][]) {

        if (mat.length != mat[0].length) {
            return false;
        }
        return true;

    }

    /**
     * En aquesta funció ens asseguram que una matriu existi i sigui vàlida per fer feina amb ella.
     *
     * @param mat matriu
     * @return true si existeix / false si no
     */
    private static boolean matrixExists(double mat[][]) {

        if (Double.isNaN(mat.length)) {
            return false;
        }

        if (mat.length == 0 || mat[0].length == 0) {
            return false;
        }

        for (int i = 0; i < mat.length - 1; i++) {
            if (mat[i].length != mat[i + 1].length) {
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
