package com.psywerx.inarow;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.Matrix;

public class Model {
    private final int BYTES_PER_FLOAT = 4;
    
    public final FloatBuffer mVertexBuffer;
    public final FloatBuffer mTextureBuffer;
    public final FloatBuffer mNormalBuffer;
    public float[] mMatrix;
    
    private Model(ArrayList<Float> vertices, ArrayList<Float> normals, ArrayList<Float> textures) {
        mVertexBuffer = ByteBuffer.allocateDirect(vertices.size() * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
        for(Float i: vertices) {
            mVertexBuffer.put(i);
        }
        mVertexBuffer.position(0);
        
        mTextureBuffer = ByteBuffer.allocateDirect(textures.size() * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
        for(Float i: textures) {
            mTextureBuffer.put(i);
        }
        mTextureBuffer.position(0);
        
        mNormalBuffer = ByteBuffer.allocateDirect(normals.size() * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
        for(Float i: normals) {
            mNormalBuffer.put(i);
        }
        mNormalBuffer.position(0);
        
        mMatrix = new float[16];
        Matrix.setIdentityM(mMatrix, 0);
    }
    
    public static Model getModel(Context context, String filename) {
        try {
            InputStreamReader src = new InputStreamReader(context.getAssets().open(filename + ".obj", AssetManager.ACCESS_STREAMING));
            BufferedReader brc = new BufferedReader(src);
            String line;

            ArrayList<Float> vertices = new ArrayList<Float>();
            ArrayList<Float> normals = new ArrayList<Float>();
            ArrayList<Float> textures = new ArrayList<Float>();
            int vertIndex, textIndex, normIndex;
            
            ArrayList<Float> resultVertices = new ArrayList<Float>();
            ArrayList<Float> resultNormals = new ArrayList<Float>();
            ArrayList<Float> resultTextures = new ArrayList<Float>();
            
            while ((line = brc.readLine()) != null) {
                if (line.startsWith("#"))
                    continue;

                String[] split = line.split(" ");
                if (split.length == 0)
                    continue;

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
                if (split[0].equals("vt")) {
                    for (int i = 1; i < split.length; i++) {
                        textures.add(Float.parseFloat(split[i]));
                    }
                }
                
                
                if (split[0].equals("f")) {
                    for (int i = 1; i < split.length; i++) {
                        String[] s = split[i].split("/");
                        vertIndex = Integer.parseInt(s[0])-1;
                        textIndex = Integer.parseInt(s[1])-1;
                        normIndex = Integer.parseInt(s[2])-1;
                        
                        resultVertices.add(vertices.get(vertIndex*3));
                        resultVertices.add(vertices.get(vertIndex*3+1));
                        resultVertices.add(vertices.get(vertIndex*3+2));
                        
                        resultTextures.add(textures.get(textIndex*2+0));
                        resultTextures.add(textures.get(textIndex*2+1));
                        
                        resultNormals.add(normals.get(normIndex*3+0));
                        resultNormals.add(normals.get(normIndex*3+1));
                        resultNormals.add(normals.get(normIndex*3+2));
                    }
                }
            }
            return new Model(resultVertices, resultNormals, resultTextures);
        } catch (Exception e) {
            return null;
        }
    }
}
