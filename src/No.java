public abstract class No {
    protected int[] vInfo;
    protected No[] vLig;
    protected No pai;
    protected int TL;

    public No(){
        this.vInfo = new int[Arvore.ordem - 1];
        this.vLig = new No[Arvore.ordem];
    }

    public void setvInfo(int pos, int info) {
        this.vInfo[pos] = info;
    }

    public int getvInfo(int pos) {
        return this.vInfo[pos];
    }

    public void setvLig(int pos, No no) {
        this.vLig[pos] = no;
    }

    public No getvLig(int pos) {
        return this.vLig[pos];
    }

    public void setTL(int TL) {
        this.TL = TL;
    }

    public int getTL() {
        return this.TL;
    }

    public void setPai(No pai) {
        this.pai = pai;
    }

    public No getPai() {
        return this.pai;
    }

    public int procurarPosicao(int info) {
        int pos = 0;
        while(pos < getTL() && info > getvInfo(pos))
            pos++;
        return pos;
    }

    public void remanejar(int pos) {
        vInfo[TL + 1] = vInfo[TL];
        for (int i = TL; i > pos; i--) {
            vInfo[i] = vInfo[i - 1];
            vLig[i] = vLig[i - 1];
        }
    }

    public boolean temQueFazerSplit(){
        return TL == Arvore.ordem - 1;
    }
}