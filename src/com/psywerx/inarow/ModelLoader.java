package com.psywerx.inarow;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import android.content.Context;
import android.content.res.AssetManager;

/**
 * 
 * Loads models from .csv source
 * 
 * @author smotko
 * 
 */
public class ModelLoader {

    public static void parseFile(Context context, String filename) {
        try {
            InputStreamReader src = new InputStreamReader(context.getAssets().open(
                    filename + ".obj", AssetManager.ACCESS_STREAMING));
            BufferedReader brc = new BufferedReader(src);
            String line;

            ArrayList<Float> vertices = new ArrayList<Float>();
            ArrayList<Float> normals = new ArrayList<Float>();
            ArrayList<Integer> faceVertices = new ArrayList<Integer>();
            ArrayList<Integer> faceNormals = new ArrayList<Integer>();

            while ((line = brc.readLine()) != null) {

                if (line.startsWith("#")) continue;

                String[] split = line.split(" ");

                if (split.length == 0) continue;

                if (split[0].equals("v")) {
                    for (int i = 1; i < split.length; i++) {
                        vertices.add(Float.parseFloat(split[i]));
                    }
                }
                if (split[0].equals("vn")) {
                    for (int i = 1; i < split.length; i++) {
                        normals.add(Float.parseFloat(split[i]));
                    }
                }
                if (split[0].equals("f")) {
                    for (int i = 1; i < split.length; i++) {
                        String[] s = split[i].split("/");
                        if (s.length == 2) {
                            faceVertices.add(Integer.parseInt(s[0])-1);
                            faceNormals.add(Integer.parseInt(s[2])-1);
                        }
                    }
                }
            }

        } catch (Exception e) {
            L.d("Error reading file: " + e.toString());
        }
    }
}