package Podjebane;

// ten wątek nie wykorzystuje iteratora
class Watek implements Runnable {

    private Fields p;
    private int x, y;

    // x, y to początkowa pozycja do iteracji
    public Watek(Fields k, int x, int y) {
        this.p = k;
        this.x = x;
        this.y = y;
    }

    public void run() {
        // klasyczna podwójna pętla do iteracji
        // tutaj kontrolujemy kolejność odwiedzin
        // zostanie to zastąpione pętlą z użyciem iteratora
        for (int i = y; i < p.getRows(); ++i) {
            int j;
            if (i == y) {
                j = x;
            } else {
                j = 0;
            }
            for (; j < p.getCols(); ++j) {
                // a w środku - obracamy, odświeżamy i czekamy
                p.repaint();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
