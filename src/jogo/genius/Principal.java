package jogo.genius;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * A classe principal do jogo inicializa o Frame principal e contém o loop do
 * jogo.
 */
public class Principal {
    public static void main(String[] args) {
        var geniusFrame = new JFrame();

        geniusFrame.setTitle("Genius");
        geniusFrame.setSize(450, 450);
        geniusFrame.setLocationRelativeTo(null);
        geniusFrame.setIgnoreRepaint(true);
        geniusFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        geniusFrame.setVisible(true);
        geniusFrame.createBufferStrategy(2);
        executarLoopDoJogo(geniusFrame);
    }

    /**
     * O jogo roda num loop. O ciclo de um jogo se divide em processar a
     * lógica, desenhar a tela e exibir o resultado do desenho para o usuário.
     * <p>
     * A classe BufferStrategy e nos fornece 2 superfícies de desenho, que são
     * alternadas toda vez que show() é chamado. Essa é uma técnica conhecida
     * como Double Buffering. Ela evita que a tela fique piscando (flicking)
     * pois, enquanto desenhamos numa superfície "escondida" o usuário vê a
     * outra superfície totalmente pintada. Só depois de pronta, alternamos
     * rapidamente as superfícies no método show().
     * <p>
     * Para mais informações sobre double buffering, leia o site:
     * http://java.sun.com/docs/books/tutorial/extra/fullscreen/doublebuf.html
     *
     * @param geniusFrame O frame para desenho
     */
    private static void executarLoopDoJogo(JFrame geniusFrame) {
        var genius = new Genius();

        // Registramos listeners para capturar eventos do frame e processa-los
        // junto ao jogo.
        registraListeners(geniusFrame, genius);

        // Sempre calculamos quandos milisegundos transcorreram entre uma
        // pintura e outra. Essa informação é importante para que possamos
        // regular a velocidade do jogo, independente da velocidade do computador.
        var ultimosMilis = System.currentTimeMillis();
        while (!genius.terminou()) {
            var segundos = (System.currentTimeMillis() - ultimosMilis) / 1000.0;
            ultimosMilis = System.currentTimeMillis();

            // No método processar executaremos a lógica do jogo.
            genius.processar(segundos);

            // Pintamos a tela.
            genius.desenhar((Graphics2D) geniusFrame.getBufferStrategy().getDrawGraphics());

            // E, finalmente, exibimos a tela pintada. A tela que era exibida
            // passa para segundo plano.
            geniusFrame.getBufferStrategy().show();

            // Damos uma pausinha para outras threads (como a do swing),
            // processarem.
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // Finaliza o frame e o programa.
        geniusFrame.dispose();
        System.exit(0);
    }

    /**
     * Apenas adicionamos listeners para tratar os eventos do Frame dentro
     * do Genius.
     *
     * @param geniusFrame O frame do jogo.
     * @param genius      O componente do jogo.
     */
    private static void registraListeners(JFrame geniusFrame, final Genius genius) {
        geniusFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                genius.processarEventoDeJanela(e);
            }
        });

        geniusFrame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                genius.processarCliqueDoMouse(e);
            }
        });
    }

}
