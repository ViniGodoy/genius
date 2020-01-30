package jogo.genius;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * Representa um botão do genius. Como você vai surpreender mais os seus colegas
 * se o botão for redondo, vamos faze-lo com esse formato.
 * <p>
 * Basicamente, um botão tem 2 estados, pressionado e solto. Chamamos o método
 * pressionar para trocar os estados. O botão fica pressionado por 1 segundo e
 * então volta para o estado não-pressionado.
 *
 * @author Vinicius
 */
public class Botao {
    private enum Estado {
        PRESSIONADO, SOLTO
    }

    private Cor cor;
    private Estado estado;
    private Ellipse2D forma;
    private double tempoPressionado;
    private Genius genius;

    // Manteremos pressionado por 1 segundo.
    private static final double MAX_TEMPO_PRESSIONADO = 1.0;

    public Botao(Genius genius, Cor cor, int x, int y, int raio) {
        this.cor = cor;
        this.forma = new Ellipse2D.Float(x, y, raio, raio);
        this.estado = Estado.SOLTO;
        this.genius = genius;
    }

    /**
     * Pressiona o botão. O pressionamento só ocorrerá se o botão estiver solto.
     */
    public void pressionar() {
        // Já está pressionado?
        if (estado == Estado.PRESSIONADO) {
            return; // Ignoramos o pressionamento
        }

        estado = Estado.PRESSIONADO;
        // Tocamos a música da cor do botão
        cor.tocarSom();
        // Começamos a contar o tempo pressionado
        tempoPressionado = 0;
    }

    /**
     * O processamento do botão é simples. Se ele estiver solto, não fazemos
     * nada. Se estiver pressionado, contamos a quanto tempo ele foi pressionado
     * e voltamos para o estado solto após 1 segundo.
     *
     * @param segundos Tempo desde a última chamada ao método.
     */
    public void processar(double segundos) {
        // Está solto? Não precisamos fazer nada.
        if (estado == Estado.SOLTO) {
            return;
        }

        // Calculamos o tempo pressionado
        tempoPressionado += segundos;

        // Se terminou o tempo, soltamos o botão
        if (tempoPressionado > MAX_TEMPO_PRESSIONADO) {
            estado = Estado.SOLTO;
            genius.botaoSoltou(this);
        }
    }

    /**
     * Desenha o botão. A cor do botão ficará mais clara se estiver pressionado.
     *
     * @param graphics Contexto gráfico onde o botão deve ser desenhado.
     */
    public void desenhar(Graphics2D graphics) {
        // Vamos garantir que as mudanças no contexto gráfico
        // não afetem outros métodos além desse. Para isso, fazemos
        // uma cópia do contexto.
        var g2d = (Graphics2D) graphics.create();

        // Desenhamos o botão. Se estiver no estado pressionado,
        // desenhamos com a cor mais clara.
        if (estado == Estado.SOLTO) {
            g2d.setColor(cor.getCorSolto());
        } else {
            g2d.setColor(cor.getCorPressionado());
        }

        // Desenhamos a eclipse
        g2d.fill(forma);

        // Liberamos a cópia do nosso contexto gráfico.
        g2d.dispose();
    }

    /**
     * Verifica se o ponto passado por parâmetro está dentro do botão.
     *
     * @param point Um ponto qualquer
     * @return True se o ponto está no botão, false se não estiver.
     */
    public boolean contem(Point2D point) {
        return forma.contains(point);
    }

    public boolean estaPressionado() {
        return estado == Estado.PRESSIONADO;
    }

    /**
     * Retorna a cor do botão.
     *
     * @return A cor do botão.
     */
    public Cor getCor() {
        return cor;
    }
}
