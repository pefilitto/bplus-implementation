public class Arvore {
    private No raiz;
    public static int ordem;

    public Arvore(int ordem) {
        Arvore.ordem = ordem;
        this.raiz = null;
    }

    public void inserir(int info) {
        if (raiz == null) {
            raiz = new NoFolha();
            raiz.setvInfo(0, info);
            raiz.setTL(1);
        }
        else {
            No folha = navegarAteFolha(info);
            int pos = folha.procurarPosicao(info);
            folha.remanejar(pos);
            folha.setvInfo(pos, info);
            folha.setTL(folha.getTL() + 1);

            if(folha.temQueFazerSplit()){
                split(folha, buscarPai(folha));
            }
        }
    }

    private void split(No no, No pai) {
        No cx1, cx2;
        int keyPromovida;

        if (no instanceof NoFolha) {
            int qtd = getQtdItensSplitFolha();
            cx1 = new NoFolha();
            cx2 = new NoFolha();
            criaCaixa1Caixa2NoFolha(no, cx1, cx2, qtd);
            keyPromovida = cx2.getvInfo(0);
        }
        else {
            int qtd = getQtdItensSplitIntermediario();
            cx1 = new NoIntermediario();
            cx2 = new NoIntermediario();
            criaCaixa1Caixa2NoIntermediario(no, cx1, cx2, qtd);
            keyPromovida = no.getvInfo(qtd);
        }

        if (pai == null) {
            NoIntermediario novaRaiz = new NoIntermediario();
            novaRaiz.setvInfo(0, keyPromovida);
            novaRaiz.setvLig(0, cx1);
            novaRaiz.setvLig(1, cx2);
            novaRaiz.setTL(1);
            raiz = novaRaiz;
        }
        else {
            int pos = pai.procurarPosicao(keyPromovida);
            pai.remanejar(pos);
            pai.setvInfo(pos, keyPromovida);
            pai.setvLig(pos, cx1);
            pai.setvLig(pos + 1, cx2);
            pai.setTL(pai.getTL() + 1);

            if (pai.temQueFazerSplit()) {
                split(pai, buscarPai(pai));
            }
        }
    }

    private void criaCaixa1Caixa2NoIntermediario(No no, No cx1, No cx2, int qtd) {
        for (int i = 0; i < qtd; i++) {
            cx1.setvInfo(i, no.getvInfo(i));
            cx1.setvLig(i, no.getvLig(i));
        }
        cx1.setvLig(qtd, no.getvLig(qtd));
        cx1.setTL(qtd);

        for (int i = qtd + 1; i < no.getTL(); i++) {
            cx2.setvInfo(i - (qtd + 1), no.getvInfo(i));
            cx2.setvLig(i - (qtd + 1), no.getvLig(i));
        }
        cx2.setvLig(no.getTL() - (qtd + 1), no.getvLig(no.getTL()));
        cx2.setTL(no.getTL() - (qtd + 1));
    }

    private void criaCaixa1Caixa2NoFolha(No no, No cx1, No cx2, int qtd) {
        for (int i = 0; i < qtd; i++) {
            cx1.setvInfo(i, no.getvInfo(i));
        }
        cx1.setTL(qtd);

        for (int i = qtd; i < no.getTL(); i++) {
            cx2.setvInfo(i - qtd, no.getvInfo(i));
        }
        cx2.setTL(no.getTL() - qtd);

        ((NoFolha) cx1).setProx((NoFolha) cx2);
        ((NoFolha) cx2).setAnt((NoFolha) cx1);
        ((NoFolha) cx1).setAnt(((NoFolha) no).getAnt());
        ((NoFolha) cx2).setProx(((NoFolha) no).getProx());

        if (((NoFolha) cx1).getAnt() != null) {
            ((NoFolha) cx1).getAnt().setProx((NoFolha)cx1);
        }
        if (((NoFolha) cx2).getProx() != null) {
            ((NoFolha) cx2).getProx().setAnt((NoFolha)cx2);
        }
    }

    public void excluir(int info) {
        if (raiz != null) {
            No folha = navegarAteFolha(info);
            int pos = encontrarPosicaoExata(folha, info);

            if (pos != -1) {
                removerItemDeFolha(folha, pos);

                if (folha == raiz) {
                    if (raiz.getTL() == 0) {
                        raiz = null;
                    }
                } else {
                    if (folha.temUnderflow()) {
                        tratarUnderflow(folha);
                    } else if (pos == 0) {
                        substituirChaveNosPais(buscarPai(folha), info, folha.getvInfo(0));
                    }

                    if (raiz.getTL() == 0 && raiz instanceof NoIntermediario) {
                        raiz = raiz.getvLig(0);
                    }
                }
            }
        }
    }

    private void tratarUnderflow(No no) {
        No pai = buscarPai(no);
        if (pai != null) {
            int posFilho = encontrarPosicaoFilho(pai, no);
            boolean redistribuiu = false;

            if (posFilho > 0) {
                No irmaoEsq = pai.getvLig(posFilho - 1);
                if (irmaoEsq.getTL() > getMinItens()) {
                    redistribuirComIrmaoEsquerdo(no, irmaoEsq, pai, posFilho);
                    redistribuiu = true;
                }
            }

            if (!redistribuiu && posFilho < pai.getTL()) {
                No irmaoDir = pai.getvLig(posFilho + 1);
                if (irmaoDir.getTL() > getMinItens()) {
                    redistribuirComIrmaoDireito(no, irmaoDir, pai, posFilho);
                    redistribuiu = true;
                }
            }

            if (!redistribuiu) {
                if (posFilho > 0) {
                    merge(pai.getvLig(posFilho - 1), no, pai, posFilho - 1);
                } else {
                    merge(no, pai.getvLig(posFilho + 1), pai, posFilho);
                }
            }
        }
    }

    private void merge(No noEsquerdo, No noDireito, No pai, int posChavePai) {
        if (noEsquerdo instanceof NoIntermediario) {
            int chavePai = pai.getvInfo(posChavePai);
            noEsquerdo.setvInfo(noEsquerdo.getTL(), chavePai);
            noEsquerdo.setTL(noEsquerdo.getTL() + 1);
        }

        for (int i = 0; i < noDireito.getTL(); i++) {
            noEsquerdo.setvInfo(noEsquerdo.getTL(), noDireito.getvInfo(i));
            if (noEsquerdo instanceof NoIntermediario) {
                noEsquerdo.setvLig(noEsquerdo.getTL(), noDireito.getvLig(i));
            }
            noEsquerdo.setTL(noEsquerdo.getTL() + 1);
        }

        if (noEsquerdo instanceof NoIntermediario) {
            noEsquerdo.setvLig(noEsquerdo.getTL(), noDireito.getvLig(noDireito.getTL()));
        }

        if (noEsquerdo instanceof NoFolha) {
            ((NoFolha) noEsquerdo).setProx(((NoFolha) noDireito).getProx());
            if (((NoFolha) noEsquerdo).getProx() != null) {
                ((NoFolha) noEsquerdo).getProx().setAnt((NoFolha) noEsquerdo);
            }
        }

        removerChaveEFilhoDireitoDoPai(pai, posChavePai);

        if (pai.temUnderflow() && pai != raiz) {
            tratarUnderflow(pai);
        }
    }

    private void redistribuirComIrmaoEsquerdo(No no, No irmaoEsq, No pai, int posPai) {
        no.remanejar(0);
        no.setTL(no.getTL() + 1);

        if (no instanceof NoFolha) {
            int itemMovido = irmaoEsq.getvInfo(irmaoEsq.getTL() - 1);
            no.setvInfo(0, itemMovido);
            pai.setvInfo(posPai - 1, itemMovido);
        } else {
            int chavePai = pai.getvInfo(posPai - 1);
            no.setvInfo(0, chavePai);
            No ligMovida = irmaoEsq.getvLig(irmaoEsq.getTL());
            no.setvLig(0, ligMovida);
            pai.setvInfo(posPai - 1, irmaoEsq.getvInfo(irmaoEsq.getTL() - 1));
        }

        irmaoEsq.setTL(irmaoEsq.getTL() - 1);
    }

    private void redistribuirComIrmaoDireito(No no, No irmaoDir, No pai, int posPai) {
        if (no instanceof NoFolha) {
            int itemMovido = irmaoDir.getvInfo(0);
            no.setvInfo(no.getTL(), itemMovido);
            removerItemDeFolha(irmaoDir, 0);
            pai.setvInfo(posPai, irmaoDir.getvInfo(0));
        } else {
            int chavePai = pai.getvInfo(posPai);
            no.setvInfo(no.getTL(), chavePai);
            No ligMovida = irmaoDir.getvLig(0);
            no.setvLig(no.getTL() + 1, ligMovida);
            pai.setvInfo(posPai, irmaoDir.getvInfo(0));
            removerPrimeiroItemDeNoIntermediario(irmaoDir);
        }
        no.setTL(no.getTL() + 1);
    }

    private void removerItemDeFolha(No folha, int pos) {
        for (int i = pos; i < folha.getTL() - 1; i++) {
            folha.setvInfo(i, folha.getvInfo(i + 1));
        }
        folha.setTL(folha.getTL() - 1);
    }

    private void removerPrimeiroItemDeNoIntermediario(No no) {
        int tl = no.getTL();
        for (int i = 0; i < tl - 1; i++) {
            no.setvInfo(i, no.getvInfo(i + 1));
        }
        for (int i = 0; i < tl; i++) {
            no.setvLig(i, no.getvLig(i + 1));
        }
        no.setTL(tl - 1);
    }

    private void removerChaveEFilhoDireitoDoPai(No pai, int posChave) {
        int tl = pai.getTL();
        for (int i = posChave; i < tl - 1; i++) {
            pai.setvInfo(i, pai.getvInfo(i + 1));
        }
        for (int i = posChave + 1; i < tl; i++) {
            pai.setvLig(i, pai.getvLig(i + 1));
        }
        pai.setTL(tl - 1);
    }

    private No navegarAteFolha(int info) {
        No no = raiz;
        while(!(no instanceof NoFolha)){
            int pos = no.procurarPosicao(info);
            no = no.getvLig(pos);
        }
        return no;
    }

    private void substituirChaveNosPais(No no, int chaveAntiga, int chaveNova) {
        if (no != null && chaveNova != -1) {
            int pos = encontrarPosicaoExata(no, chaveAntiga);
            if (pos != -1) {
                no.setvInfo(pos, chaveNova);
            } else {
                substituirChaveNosPais(buscarPai(no), chaveAntiga, chaveNova);
            }
        }
    }

    private No buscarPai(No no) {
        No pai = null;
        if(no != raiz) {
            pai = buscarPaiRecursivo(raiz, no);
        }
        return pai;
    }

    private No buscarPaiRecursivo(No atual, No procurado) {
        No paiEncontrado = null;
        if (atual != null && !(atual instanceof NoFolha)) {
            int i = 0;
            while (paiEncontrado == null && i <= atual.getTL()) {
                if (atual.getvLig(i) == procurado) {
                    paiEncontrado = atual;
                }
                i++;
            }

            i = 0;
            while (paiEncontrado == null && i <= atual.getTL()) {
                paiEncontrado = buscarPaiRecursivo(atual.getvLig(i), procurado);
                i++;
            }
        }
        return paiEncontrado;
    }

    private int encontrarPosicaoFilho(No pai, No filho) {
        int pos = -1;
        int i = 0;
        while(i <= pai.getTL() && pos == -1) {
            if (pai.getvLig(i) == filho) {
                pos = i;
            }
            i++;
        }
        return pos;
    }

    private int encontrarPosicaoExata(No no, int info) {
        int pos = -1;
        int i = 0;
        while(i < no.getTL() && pos == -1) {
            if (no.getvInfo(i) == info) {
                pos = i;
            }
            i++;
        }
        return pos;
    }

    private int getMinItens() {
        return (int) Math.ceil((double) Arvore.ordem / 2.0) - 1;
    }

    private int getQtdItensSplitFolha(){
        return (int) Math.ceil((double) Arvore.ordem / 2.0);
    }

    private int getQtdItensSplitIntermediario(){
        return (int) Math.ceil((double) Arvore.ordem / 2.0) - 1;
    }

    public void in_ordem () {
        if (raiz == null) {
            System.out.println("Árvore vazia");
        }
        else {
            No no = raiz;
            while(!(no instanceof NoFolha)){
                no = no.getvLig(0);
            }

            while(no != null){
                for(int i=0; i < no.getTL(); i++){
                    System.out.print(no.getvInfo(i) + " ");
                }
                no = ((NoFolha) no).getProx();
            }
            System.out.println();
        }
    }
}