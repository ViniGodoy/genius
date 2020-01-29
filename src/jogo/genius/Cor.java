package jogo.genius;

import java.applet.AudioClip;
import java.awt.*;
import java.util.Random;

/**
 * Nessa enumeração, dizemos quais são as cores dos botões e que som cada uma
 * delas faz. Também fornecemos um método estático para sortear cores.
 * <p>
 * A classe AudioClip é usada para tocar os sons correspondentes a cada cor.
 * Para mais informações sobre o AudioClip veja:
 * http://www.onjava.com/pub/a/onjava/excerpt/jenut3_ch17/index.html
 *
 * @author Vinicius
 */
public enum Cor {
    AMARELO(Color.YELLOW, "/recursos/som1.wav"),
    VERMELHO(Color.RED, "/recursos/som2.wav"),
    VERDE(Color.GREEN, "/recursos/som3.wav"),
    AZUL(Color.BLUE, "/recursos/som4.wav");

    private static final Random RND = new Random();

    private Color cor;
    private AudioClip som;

    Cor(Color cor, String som) {
        this.cor = cor;
        this.som = java.applet.Applet.newAudioClip(getClass().getResource(som));
    }

    /**
     * Usado para sortear uma cor.
     *
     * @return Uma cor sorteada.
     */
    public static Cor sortear() {
        var ind = RND.nextInt(values().length);
        return values()[ind];
    }

    /**
     * Reproduz o som.
     */
    public void tocarSom() {
        som.play();
    }

    /**
     * Retorna a color correspondente dessa cor.
     *
     * @return Uma Color, para ser usada pelo java.
     */
    public Color getCorPressionado() {
        return cor;
    }

    public Color getCorSolto() {
        return cor.darker().darker();
    }
}
