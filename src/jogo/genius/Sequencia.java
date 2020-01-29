package jogo.genius;

import java.util.ArrayList;
import java.util.List;

public class Sequencia {
    private List<Cor> sequencia = new ArrayList<>();
    private int indiceSequencia = -1;

    public Sequencia() {
        // Iniciamos com uma nota.
        adicionar();
    }

    public void adicionar() {
        sequencia.add(Cor.sortear());
    }

    public boolean estaCorreta(int indice, Cor corPressionadaPeloJogador) {
        return sequencia.get(indice).equals(corPressionadaPeloJogador);
    }

    public void limpar() {
        sequencia.clear();
    }

    public void processar(List<Botao> botoes) {
        // A sequencia ainda não começou?
        if (indiceSequencia == -1) {
            // Começamos a sequencia
            indiceSequencia++;
            return;
        }

        // Pegamos o botão correspondente a cor da sequencia
        int indBotao = sequencia.get(indiceSequencia).ordinal();

        // Se o último botão ainda estiver pressionado, não há nada a fazer...
        // Temos que esperar ele soltar para pressionar o próximo botão.
        int indUltBotao = indiceSequencia == 0 ? -1 : sequencia.get(
                indiceSequencia - 1).ordinal();
        if (indUltBotao >= 0 && botoes.get(indUltBotao).estaPressionado())
            return;

        // Caso contrário, pressionamos ele e passamos para o próximo
        // botão da sequencia
        botoes.get(indBotao).pressionar();
        indiceSequencia++;

        // Se a sequencia acabou, reiniciamos o índice.
        if (indiceSequencia == sequencia.size())
            indiceSequencia = -1;
    }

    public boolean acabou() {
        return indiceSequencia == -1;
    }

    public int tamanho() {
        return sequencia.size();
    }
}
