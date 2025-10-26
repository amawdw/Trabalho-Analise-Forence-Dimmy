package br.edu.icev.aed.forense;

import java.io.*;
import java.util.*;

public class AnalistaForense implements AnaliseForenseAvancada{
    @Override
    public Set<String> encontrarSessoesInvalidas(String caminhoArquivo) throws IOException {
        return Set.of();
    }

    @Override
    public List<String> reconstruirLinhaTempo(String caminhoArquivo, String sessionId) throws IOException {
        return List.of();
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

//Desafio 2
@Override
public List<String> reconstruirLinhaTempo(String caminhoArquivo, String sessionId) throws IOException {
    // Fila para manter a ordem cronológica dos ACTION_TYPE
    Deque<String> fila = new ArrayDeque<>();

    try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
        String linha;
        boolean primeira = true;
        while ((linha = br.readLine()) != null) {
            if (linha.isEmpty()) continue;

            // Pular o header, se existir
            if (primeira && linha.toUpperCase(Locale.ROOT).startsWith("TIMESTAMP,")) {
                primeira = false;
                continue;
            }
            primeira = false;

            // Esperamos 7 colunas: TIMESTAMP,USER_ID,SESSION_ID,ACTION_TYPE,TARGET_RESOURCE,SEVERITY_LEVEL,BYTES_TRANSFERRED
            String[] cols = linha.split(",", 7);
            if (cols.length < 7) continue; // linha malformada → ignora

            String sess = cols[2];
            if (!sessionId.equals(sess)) continue;

            String action = cols[3];
            fila.addLast(action); // enfileira na ordem de leitura
        }
    }

    // Desenfileira para uma lista (ordem cronológica estrita)
    List<String> resultado = new ArrayList<>(fila.size());
    while (!fila.isEmpty()) {
        resultado.add(fila.removeFirst());
    }
    return resultado; // pode ser vazia; nunca null
}
}

