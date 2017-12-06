package simulation.puzzle;

import java.awt.*;
import java.util.*;

/**
 * Created by kifkif on 30/10/2017.
 */
public class GradientHelper {
    public static int[][] initGradient(int[][] gradient)
    {
        for(int i = 0; i < gradient.length; i++){
            for(int j = 0; j < gradient[i].length; i++){
                gradient[i][j] = Integer.MAX_VALUE;
            }
        }

        return gradient;
    }

    public static int[][] calcGradient(int[][] gradient, Dimension loc, Dimension dim)
    {
        int grad = 0;
        java.util.List<Dimension> locs = new ArrayList<>();
        locs.add(loc);

        while (!locs.isEmpty())
        {
            Dimension curLoc = locs.get(0);
            locs.remove(0);
            if(updateGradientAt(gradient, curLoc, grad))
            {
                locs.add(new Dimension((int)curLoc.getWidth()+1, (int)curLoc.getHeight()));
                locs.add(new Dimension((int)curLoc.getWidth()-1, (int)curLoc.getHeight()));
                locs.add(new Dimension((int)curLoc.getWidth(), (int)curLoc.getHeight()+1));
                locs.add(new Dimension((int)curLoc.getWidth(), (int)curLoc.getHeight()-1));
            }
        }

        return gradient;
    }

    public static boolean updateGradientAt(int[][] gradient, Dimension loc, int grad)
    {
        if(inMap(gradient, loc) && gradient[(int)loc.getWidth()][(int)loc.getHeight()] > grad)
        {
            gradient[(int)loc.getWidth()][(int)loc.getHeight()] = grad;
            return true;
        }

        return false;
    }

    private static boolean inMap(int[][] gradient, Dimension loc) {
        return gradient.length < loc.getWidth() && loc.getWidth() >= 0 && gradient[0].length < loc.getHeight() && loc.getHeight() >= 0;
    }
}
