public class NoFolha extends No {
    private NoFolha prox;
    private NoFolha ant;

    public NoFolha(){
        super();
        this.prox = null;
        this.ant = null;
    }

    public NoFolha getProx() {
        return prox;
    }

    public void setProx(NoFolha prox) {
        this.prox = prox;
    }

    public NoFolha getAnt() {
        return ant;
    }

    public void setAnt(NoFolha ant) {
        this.ant = ant;
    }
}
