import java.util.*;
import java.lang.*;

import java.awt.*;  
import javax.swing.*;  
import java.awt.geom.*;  

public class HasseDrawing extends JPanel{

    static String input;
    static int SCALE = 80;
    static int radius = 20;

    static java.util.List<java.util.List<Integer>> adjlist;
    static java.util.List<java.util.List<Integer>> adjlist2;
    static java.util.List<java.util.List<Integer>> adjlist3;
    static java.util.List<Integer> bases;

    static java.util.List<Vector2D> posOfBases;

    Vector2D origin;

    static Graphics2D graph;

    static void print(java.util.List<Integer> roots,java.util.List<java.util.List<Integer>> data){
        int n = roots.size();
        for(int i = 0;i < n;i ++){
            System.out.print(roots.get(i) + " :");
            
            for(int elt : data.get(i)){
                System.out.print(" " + elt);
            }
            
            System.out.println();
        }
    }
    
    static void printPos(java.util.List<Integer> bases ,java.util.List<Vector2D> vecs){
        int base;
        Vector2D vec;
        for(int i = 0;i < bases.size();i++){
            base = bases.get(i);
            vec = vecs.get(i);
            System.out.println(base + " : ( " + vec.getX() + " , " + vec.getY() + " )");
        }
    }

    protected void paintComponent(Graphics grf){
        super.paintComponent(grf); 

        adjlist = new java.util.ArrayList<>();
        adjlist2 = new java.util.ArrayList<>();
        adjlist3 = new java.util.ArrayList<>();

        bases = new java.util.ArrayList<>();

        origin = new Vector2D(0,500);

        graph = (Graphics2D)grf;

        /*
        EXAMPLE :
        ---------
        
        INPUT:
        No of elements : 7
        
        Values :
        1 : 1 2 3 4 6 8 12
        2 : 2 4 6 8 12
        3 : 3 6 12
        4 : 4 8 12
        6 : 6 12
        8 : 8
        12 : 12

        _HASSE1
        1 : 1 2 3 4 6 8 12
        2 : 2 4 6 8 12
        3 : 3 6 12
        4 : 4 8 12
        6 : 6 12
        8 : 8
        12 : 12

        _HASSE2
        1 : 2 3 4 6 8 12
        2 : 4 6 8 12
        3 : 6 12
        4 : 8 12
        8 : 
        12 : 

        _HASSE3
        1 : 2 3 
        2 : 4 6 
        3 : 6 
        4 : 8 12
        6 : 12
        8 : 
        12 : 
        */
        Scanner in = new Scanner(input);
        Scanner sc;

        while(in.hasNextLine()){
            sc = new Scanner(in.nextLine());
            
            adjlist.add(new java.util.ArrayList<Integer>());
            adjlist2.add(new java.util.ArrayList<Integer>());
            adjlist3.add(new java.util.ArrayList<Integer>());

            bases.add(sc.nextInt());
            
            while(sc.hasNextInt()){
                int inp = sc.nextInt();
                adjlist.get(adjlist.size()-1).add(inp);
                adjlist2.get(adjlist.size()-1).add(inp);
                adjlist3.get(adjlist.size()-1).add(inp);
            }
            
            sc.reset();
        }

        System.out.println("\nHASSE 1");
        print(bases,adjlist);

        for(int i = 0;i < bases.size();i ++){
            adjlist2.get(i).remove(bases.get(i));
            adjlist3.get(i).remove(bases.get(i));
        }
        
        System.out.println("\nHASSE 2");
        print(bases,adjlist2);
        
        for(int i = 0; i < adjlist.size();i ++){
            java.util.List<Integer> list = adjlist3.get(i);
            
            for(int j = 0; j < list.size(); j++){
                int elt = list.get(j);
                
                for(int k = 0; k < adjlist2.get(bases.indexOf(elt)).size(); k++){
                    int elt2 = adjlist2.get(bases.indexOf(elt)).get(k);
                    
                    if(list.indexOf(elt2) != -1)
                        list.remove(list.indexOf(elt2));
                }
            }
        }
        System.out.println("\nHASSE 3");
        print(bases,adjlist3);
        
        java.util.List<Integer> roots = new java.util.ArrayList<>();
        
        boolean flag = false;
        for(int base : bases){
            for(java.util.List <Integer> list : adjlist3){
                if(list.indexOf(base) != -1 && !flag){
                    flag = true;
                }
            }
            if(!flag)
                roots.add(base);
            flag = false;
        }
        System.out.println("\nROOTS = " + roots);
        
        posOfBases = new java.util.ArrayList<>();
        java.util.List<Integer> basesInCurrLvl = new java.util.ArrayList<>();
        java.util.List<Integer> basesInPrevLvl = roots;
        
        for(int i = 0; i < bases.size(); i++){
            posOfBases.add(new Vector2D(0,0));
        }
        
        for(int i = 0; i < roots.size(); i ++){
            posOfBases.get(bases.indexOf(roots.get(i))).set(SCALE * (1 + i) , SCALE);
        }
        
        System.out.println("\nPOS OF ROOTS : ");
        printPos(bases,posOfBases);
        
        int currLvl = 2;
        int currSize = 1;
        
        while(basesInPrevLvl.size() != 0){
        for(int i = 0;i<basesInPrevLvl.size();i++){
            int base = basesInPrevLvl.get(i);
            for(int j = 0;j<adjlist3.get(bases.indexOf(base)).size();j++){
                int base2 = adjlist3.get(bases.indexOf(base)).get(j);
                
                if(basesInCurrLvl.indexOf(base2) == -1){
                    basesInCurrLvl.add(base2);
                
                    posOfBases.get(bases.indexOf(base2)).set(SCALE * basesInCurrLvl.size() , SCALE * currLvl);
                    currSize++;
                }
            }
        }
            basesInPrevLvl.clear();
            basesInPrevLvl = basesInCurrLvl;
            basesInCurrLvl = new java.util.ArrayList<>();
            currLvl++;
            currSize = 1;
        }
        System.out.println("\nPOS OF BASES : ");
        printPos(bases,posOfBases);
        
        System.out.println("\nI am at last :) ");

        //DRAW ALL

        graph.setPaint(Color.BLUE);
        graph.drawString("HASSE 1",origin.getX() + SCALE, origin.getY());
        drawHasse(grf, adjlist);

        origin.set(origin.getX() + SCALE*4,origin.getY());
        graph.setPaint(Color.BLUE);
        graph.drawString("HASSE 2",origin.getX() + SCALE, origin.getY());
        drawHasse(grf,adjlist2);

        origin.set(origin.getX() + SCALE*4,origin.getY());
        graph.setPaint(Color.BLUE);
        graph.drawString("HASSE 3",origin.getX() + SCALE, origin.getY());
        drawHasse(grf, adjlist3);
    }

    public static void main(String args[]){
        Scanner in = new Scanner(System.in);

        System.out.print("No of elements : ");
        int n = in.nextInt();

        input = "";

        System.out.println("\nEnter values : ");
        for(int i = 0; i <= n; i ++){
            input += in.nextLine() + "\n";
        }
        
        input = input.trim();
        input = input.replace(" : "," ");
        
        JFrame frame = new JFrame();  
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame.add(new DS_A1_P1());  
        frame.setSize(400,400);  
        frame.setLocation(200, 200);  
        frame.setVisible(true);
    }

    void drawHasse(Graphics grf, java.util.List<java.util.List<Integer>> adjlistx){ 
          
        graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        graph.setPaint(Color.RED);

        for(int i = 0;i < bases.size(); i ++){
            for(int elt : adjlistx.get(i)){
                Vector2D fromVec = posOfBases.get(i);
                Vector2D toVec   = posOfBases.get(bases.indexOf(elt));
                if(fromVec.getX() == toVec.getX() && fromVec.getY() == toVec.getY()){
                    graph.draw(new Ellipse2D.Double(origin.getX() + fromVec.getX() + ARR_SIZE / 2, origin.getY() - fromVec.getY(), 2 * radius, 2 * radius));
                    // Draw arrow head as calc
                    drawArrow(grf, origin.getX() + fromVec.getX() + ARR_SIZE, origin.getY() - fromVec.getY(), origin.getX() + fromVec.getX(), origin.getY() - fromVec.getY());
                }else{
                    drawArrow(grf, origin.getX() + fromVec.getX(), origin.getY() - fromVec.getY(), origin.getX() + toVec.getX(), origin.getY() - toVec.getY());
                    // Draw arrow head at pos angle = 90
                }
            }
        }

        // draw green circles with blue text
        for(int i = 0;i < bases.size(); i ++){
            Vector2D atVec = posOfBases.get(i);

            graph.setPaint(Color.GREEN);
            graph.fill(new Ellipse2D.Double(origin.getX() + atVec.getX()- radius, origin.getY() - atVec.getY() - radius, 2 * radius, 2 * radius));

            graph.setPaint(Color.WHITE);
            graph.drawString(bases.get(i).toString(),origin.getX() + atVec.getX() - 4,origin.getY() - atVec.getY() + 4);
        }
    }

    private final int ARR_SIZE = 8;
    void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
        Graphics2D g = (Graphics2D) g1.create();

        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);

        int len = (int) Math.sqrt(dx*dx + dy*dy);

        AffineTransform at = AffineTransform.getTranslateInstance(x1 - radius*Math.cos(angle), y1 - radius*Math.sin(angle));
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);

        // Draw horizontal arrow starting in (0, 0)
        g.drawLine(0, 0, len, 0);
        g.fillPolygon(new int[] {len, len-ARR_SIZE, len-ARR_SIZE, len},new int[] {0, -ARR_SIZE/2, ARR_SIZE/2, 0}, 4);
    }     
}

class Vector2D{
    int X;
    int Y;
    
    Vector2D(int x,int y){
        X = x;
        Y = y;
    }
    
    int getX(){
        return X;
    }
    
    int getY(){
        return Y;
    }
    
    void set(int x,int y){
        X = x;
        Y = y;
    }
}
