/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author yannick
 */
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

// les threads de la classe Producteur
// découpent r et s en 10 fragments
class Producteur extends Solution2 {
    int r[] = null;
    int s[] = null;
    int debutR, finR, debutS, finS;

    public Producteur(List uneListeR, List uneListeS,
            int unR[], int unS[], int unDebutR, int uneFinR,
            int unDebutS, int uneFinS) {
        super(uneListeR, uneListeS, null);
        this.r = unR;
        this.s = unS;
        this.debutR = unDebutR;
        this.debutS = unDebutS;
        this.finR = uneFinR;
        this.finS = uneFinS;
    }

    public void run() {
        for (int i = debutR; i < finR; i++) {
            int j = r[i] % 10;
            synchronized (R.get(j)) {
                ((List) R.get(j)).add(r[i]);
                R.get(j).notify();
            }
        }
        for (int i = debutS; i < finS; i++) {
            int j = s[i] % 10;
            synchronized (S.get(j)) {
                ((List) S.get(j)).add(s[i]);
                S.get(j).notify();
            }
        }
    }
}

public class Solution2 extends Thread {

    public List R, S, T;

    public Solution2(List unR, List unS, List unT) {
        this.R = unR;
        this.S = unS;
        this.T = unT;
    }

    public void run() {
        int tailleR = this.R.size();
        int tailleS = this.S.size();
        for (int i = 0; i < tailleR; i++) {
            if (S.contains(R.get(i))) {
                T.add(R.get(i));
            }
        }
        for (int i = 0; i < tailleS; i++) {
            if (R.contains(S.get(i))) {
                if (!T.contains(S.get(i))) {
                    T.add(S.get(i));
                }
            }
        }
    }

    public static void main(String args[]) {

        int[] r = {1, 3, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31, 33, 35, 37, 39, 41, 43, 45, 47, 49, 51, 53, 55, 57, 59, 61, 63, 65, 67, 69, 71, 73, 75, 77, 79, 81, 83, 85, 87, 89, 91, 93, 95, 97, 99};
        int[] s = {1, 2, 5, 6, 9,
            10, 13, 14, 17, 18,
            21, 22, 25, 26, 29,
            30, 33, 34, 37, 38,
            41, 42, 45, 46, 49,
            50, 53, 54, 57, 58,
            61, 62, 65, 66, 69,
            70, 73, 74, 77, 78,
            81, 82, 85, 86, 89,
            90, 93, 94, 97, 100};

        List R = new ArrayList();
        List S = new ArrayList();
        List T = new ArrayList();

        for (int i = 0; i < 10; i++) {
            R.add(new ArrayList());
            S.add(new ArrayList());
            T.add(new ArrayList());
        }

        int tailleR = r.length;
        int tailleFragmentsR = tailleR / 10;
        int tailleS = s.length;
        int tailleFragmentsS = tailleS / 10;

        List threads = new ArrayList();
        for (int i = 0; i < 9; i++) {
            threads.add(new Producteur(R, S, r, s,
                    tailleFragmentsR * i, (tailleFragmentsR * (i + 1)) - 1,
                    tailleFragmentsS * i, (tailleFragmentsS * (i + 1)) - 1));
        }
        threads.add(new Producteur(R, S, r, s,
                tailleFragmentsR * 9, tailleR,
                tailleFragmentsS * 9, tailleS));
        
        for (int i = 0; i < 10; i++) {
            ((Thread) threads.get(i)).start();
        }
        for (int i = 0; i < 10; i++) {
            try {
                ((Thread) threads.get(i)).join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Solution2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        threads = new ArrayList();
        for (int i = 0; i < 10; i++) {
            List ri = (List) R.get(i);
            List si = (List) S.get(i);
            List ti = (List) T.get(i);
            threads.add(new Solution2(ri, si, ti));
            ((Thread) threads.get(i)).start();
        }
        for (int i = 0; i < 10; i++) {
            try {
                ((Thread) threads.get(i)).join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Solution2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

//        System.out.println(T);

        List resultat = new ArrayList();
        for (int i = 0; i < 10; i++) {
            resultat.add(T.get(i));
        }

        System.out.println(resultat);
    }
}
