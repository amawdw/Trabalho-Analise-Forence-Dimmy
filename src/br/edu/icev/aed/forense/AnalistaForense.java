package br.edu.icev.aed.forense;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
}
