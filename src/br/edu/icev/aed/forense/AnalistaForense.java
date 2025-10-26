package br.edu.icev.aed.forense;

import java.io.*;
import java.util.*;

public class AnalistaForense implements AnaliseForenseAvancada{
    @Override
    public Set<String> encontrarSessoesInvalidas(String caminhoArquivo) throws IOException {
        return Set.of();
    }

    @Override
    public List<Alerta> priorizarAlertas(String caminhoArquivo, int n) throws IOException {
        return List.of();
    }

    @Override
    public Map<Long, Long> encontrarPicosTransferencia(String caminhoArquivo) throws IOException {
        return Map.of();
    }

    @Override
    public Optional<List<String>> rastrearContaminacao(String caminhoArquivo, String recursoInicial, String recursoAlvo) throws IOException {
        return Optional.empty();
    }



//Desafio 2 - Reconstroi a linha do tempo das açoes de uma sessao
@Override
public List<String> reconstruirLinhaTempo(String caminhoArquivo, String sessionId) throws IOException {
   //usei uma fila pra manter a ordem cronologica das açoes
    Deque<String> fila = new ArrayDeque<>();

    // abre o arquivo pra leitura
    try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
        String linha;
        boolean primeira = true; //pra pular o cabeçalho se tiver

        while ((linha = br.readLine()) != null) {
            if (linha.isEmpty()) continue;

            // pula a primeira linha se for o cabeçalho do CSV
            if (primeira && linha.toUpperCase(Locale.ROOT).startsWith("TIMESTAMP,")) {
                primeira = false;
                continue;
            }
            primeira = false;

            //separa as linhas das colunas
            String[] cols = linha.split(",", 7);
            if (cols.length < 7) continue; //se < 7 ignora

            if (!sessionId.equals(cols[2])) continue;
            fila.addLast(cols[3]);
        }
    }
    //transforma fila em lista na ordem certinha
    List<String> resultado = new ArrayList<>(fila.size());
    while (!fila.isEmpty()) resultado.add(fila.removeFirst());
    return resultado;
}
}

