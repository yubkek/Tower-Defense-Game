package WizardTD;

import java.util.ArrayList;

public class PathMake {
    /**
     * Returns an arraylist of float arrays that indicate the different points the enemy should travel to, representing a path.
     * @return ArrayList of float array for level 1 path.
     */
    public ArrayList<float[]> level1path() {
        ArrayList<float[]> toReturn = new ArrayList<>();
        toReturn.add(new float[]{-1f,3f});
        toReturn.add(new float[]{4f,3f});
        toReturn.add(new float[]{4f,5f});
        toReturn.add(new float[]{16f,5f});
        toReturn.add(new float[]{16f,8f});
        toReturn.add(new float[]{9f,8f});
        return toReturn;
    }
    /**
     * Returns an arraylist of float arrays that indicate the different points the enemy should travel to, representing a path.
     * @return ArrayList of float array for level 2 path 1.
     */
    public ArrayList<float[]> level2path1() {
        ArrayList<float[]> toReturn = new ArrayList<>();
        toReturn.add(new float[]{-1f,3f});
        toReturn.add(new float[]{4f,3f});
        toReturn.add(new float[]{4f,5f});
        toReturn.add(new float[]{16f,5f});
        toReturn.add(new float[]{16f,8f});
        toReturn.add(new float[]{10f,8f});
        toReturn.add(new float[]{10f,14f});
        toReturn.add(new float[]{3f,14f});
        return toReturn;
    }
    /**
     * Returns an arraylist of float arrays that indicate the different points the enemy should travel to, representing a path.
     * @return ArrayList of float array for level 2 path 2.
     */
    public ArrayList<float[]> level2path2() {
        ArrayList<float[]> toReturn = new ArrayList<>();
        toReturn.add(new float[]{9f,-1f});
        toReturn.add(new float[]{9f,5f});
        toReturn.add(new float[]{16f,5f});
        toReturn.add(new float[]{16f,8f});
        toReturn.add(new float[]{10f,8f});
        toReturn.add(new float[]{10f,14f});
        toReturn.add(new float[]{3f,14f});
        return toReturn;
    }
    /**
     * Returns an arraylist of float arrays that indicate the different points the enemy should travel to, representing a path.
     * @return ArrayList of float array for level 3 path 1.
     */
    public ArrayList<float[]> level3path1() {
        ArrayList<float[]> toReturn = new ArrayList<>();
        toReturn.add(new float[]{16f,-1f});
        toReturn.add(new float[]{16f,8f});
        toReturn.add(new float[]{10f,8f});
        toReturn.add(new float[]{6f,8f});
        toReturn.add(new float[]{6f,13f});
        return toReturn;
    }
    /**
     * Returns an arraylist of float arrays that indicate the different points the enemy should travel to, representing a path.
     * @return ArrayList of float array for level 3 path 2.
     */
    public ArrayList<float[]> level3path2() {
        ArrayList<float[]> toReturn = new ArrayList<>();
        toReturn.add(new float[]{20f,5f});
        toReturn.add(new float[]{10f,5f});
        toReturn.add(new float[]{10f,8f});
        toReturn.add(new float[]{6f,8f});
        toReturn.add(new float[]{6f,13f});
        return toReturn;
    }
    /**
     * Returns an arraylist of float arrays that indicate the different points the enemy should travel to, representing a path.
     * @return ArrayList of float array for level 3 path 3.
     */
    public ArrayList<float[]> level3path3() {
        ArrayList<float[]> toReturn = new ArrayList<>();
        toReturn.add(new float[]{6f,20f});
        toReturn.add(new float[]{6f,17f});
        toReturn.add(new float[]{10f,17f});
        toReturn.add(new float[]{10f,8f});
        toReturn.add(new float[]{6f,8f});
        toReturn.add(new float[]{6f,13f});
        return toReturn;
    }
    /**
     * Returns an arraylist of float arrays that indicate the different points the enemy should travel to, representing a path.
     * @return ArrayList of float array for level 3 path 4.
     */
    public ArrayList<float[]> level3path4() {
        ArrayList<float[]> toReturn = new ArrayList<>();
        toReturn.add(new float[]{14f,20f});
        toReturn.add(new float[]{14f,17f});
        toReturn.add(new float[]{10f,17f});
        toReturn.add(new float[]{10f,8f});
        toReturn.add(new float[]{6f,8f});
        toReturn.add(new float[]{6f,13f});
        return toReturn;
    }
    /**
     * Returns an arraylist of float arrays that indicate the different points the enemy should travel to, representing a path.
     * @return ArrayList of float array for level 4 path 1.
     */
    public ArrayList<float[]> level4path1() {
        ArrayList<float[]> toReturn = new ArrayList<>();
        toReturn.add(new float[]{-1f,8f});
        toReturn.add(new float[]{10f,8f});
        toReturn.add(new float[]{10f,15f});
        return toReturn;
    }
    /**
     * Returns an arraylist of float arrays that indicate the different points the enemy should travel to, representing a path.
     * @return ArrayList of float array for level 4 path 2.
     */
    public ArrayList<float[]> level4path2() {
        ArrayList<float[]> toReturn = new ArrayList<>();
        toReturn.add(new float[]{9f,-1f});
        toReturn.add(new float[]{9f,5f});
        toReturn.add(new float[]{16f,5f});
        toReturn.add(new float[]{16f,8f});
        toReturn.add(new float[]{10f,8f});
        toReturn.add(new float[]{10f,15f});
        return toReturn;
    }
    /**
     * Returns an arraylist of float arrays that indicate the different points the enemy should travel to, representing a path.
     * @return ArrayList of float array for level 4 path 3.
     */
    public ArrayList<float[]> level4path3() {
        ArrayList<float[]> toReturn = new ArrayList<>();
        toReturn.add(new float[]{20f,5f});
        toReturn.add(new float[]{16f,5f});
        toReturn.add(new float[]{16f,8f});
        toReturn.add(new float[]{10f,8f});
        toReturn.add(new float[]{10f,15f});
        return toReturn;
    }

}
