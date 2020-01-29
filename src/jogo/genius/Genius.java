package jogo.genius;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * O genius tem 2 estados. O primeiro é tocando um som para o jogador. O
 * segundo, esperando o jogador reproduzir o som.
 *
 * @author Vinicius
 */
public class Genius {
    private enum Estado {
        TOCANDO, OUVINDO
    }

    private Estado estado = Estado.TOCANDO;
    private Sequencia sequencia = new Sequencia();

    // Lista com todos os botões do jogo
    private List<Botao> botoes = new ArrayList<>();

    // Indica se o jogo já terminou definitivamente ou não.
    private boolean terminou = false;

    // Testa o último botão pressionado pelo jogador
    private Cor ultCorPressionada = null;

    // Indica se existe um botão pressionado
    private boolean botaoPressionado = false;

    // Conta quantos pressionamentos de botões o jogador já fez.
    private int indPressionamento = -1;

    public Genius() {
        // Coordenada dos botões.
        int[] x = new int[]{50, 300, 50, 300};
        int[] y = new int[]{50, 50, 300, 300};

        // Cria um vetor com os botões.
        Cor[] cores = Cor.values();
        for (var i = 0; i < cores.length; i++)
            botoes.add(new Botao(this, cores[i], x[i], y[i], 100));
    }

    /**
     * Esse método deve retornar true quando o programa acabar.
     */
    public boolean terminou() {
        return terminou;
    }

    private boolean temBotaoPressionado() {
        for (var botao : botoes)
            if (botao.estaPressionado())
                return true;

        return false;
    }

    // PROCESSAMENTO DE EVENTOS
    public void processarCliqueDoMouse(MouseEvent e) {
        // Se estamos tocando o som para o jogador, ignoramos cliques com o
        // mouse.
        if (estado == Estado.TOCANDO) {
            return;
        }


        // Ignora pressionamentos se já tem alguém pressionado.
        if (temBotaoPressionado())
            return;

        // Caso contrário, capturamos o clique do jogador.
        // E verificamos se a sequencia está correta.

        // Para isso, procuramos qual botão foi clicado e o pressionamos.
        for (var botao : botoes)
            if (botao.contem(e.getPoint())) {
                indPressionamento++;
                botao.pressionar();
                botaoPressionado = true;
            }
    }

    public void processarEventoDeJanela(WindowEvent e) {
        // Matamos o jogo impiedosamente.
        // Depois você poderia programar uma confirmação
        terminou = true;
    }

    // PROCESSAMENTO DO JOGO

    public void processar(double segundos) {
        for (var btn : botoes)
            btn.processar(segundos);

        switch (estado) {
            case TOCANDO:
                //Processamos o próximo botão da sequencia
                sequencia.processar(botoes);

                //Se a sequencia terminou, voltamos a ouvir o jogador
                if (sequencia.acabou())
                    trocarEstado();
                break;

            case OUVINDO:
                // Aguardamos o último botão ser solto.
                if (botaoPressionado)
                    return;

                // Aguardamos o jogador pressionar o primeiro botão
                if (indPressionamento == -1)
                    return;

                // Verificamos o último botão pressionado é o correto.
                // Se não for, terminamos o jogo.
                if (!sequencia.estaCorreta(indPressionamento, ultCorPressionada)) {
                    JOptionPane.showMessageDialog(null, "Sequencia errada!");
                    terminou = true;
                    return;
                }

                // Se o jogador terminou a sequencia, alteramos para o estado
                // TOCANDO.
                if (indPressionamento == sequencia.tamanho() - 1) {
                    trocarEstado();
                    return;
                }

                break;
        }
    }

    private void trocarEstado() {
        if (estado == Estado.TOCANDO) {
            estado = Estado.OUVINDO;
            indPressionamento = -1;
            ultCorPressionada = null;
            return;
        }

        if (estado == Estado.OUVINDO) {
            // SE o jogador tocou tudo certo e voltamos ao estado tocando,
            // adicionamos mais uma nota na sequencia
            estado = Estado.TOCANDO;
            sequencia.adicionar();
        }
    }

    public void desenhar(Graphics2D graphics2D) {
        // Vamos começar apagando a tela. Que tal fundo preto?
        graphics2D.setBackground(Color.BLACK);
        graphics2D.clearRect(0, 0, 640, 480);

        // Agora, vamos desenhar os 4 botões
        for (Botao botao : botoes)
            botao.desenhar(graphics2D);
    }

    /**
     * O botao informa ao genius que soltou através desse método.
     *
     * @param botao Botão que se soltou.
     */
    public void botaoSoltou(Botao botao) {
        if (estado == Estado.TOCANDO)
            return;

        botaoPressionado = false;
        ultCorPressionada = botao.getCor();
    }

}
