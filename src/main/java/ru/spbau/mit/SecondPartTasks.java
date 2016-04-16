package ru.spbau.mit;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SecondPartTasks {

    private static final long NUMBER_OF_EXPERIMENTS = 10000;

    private SecondPartTasks() {}

    // Найти строки из переданных файлов, в которых встречается указанная подстрока.
    public static List<String> findQuotes(List<String> paths, CharSequence sequence) {
        return paths
                .stream()
                .flatMap(SecondPartTasks::getLines)
                .filter(l -> l.contains(sequence))
                .collect(Collectors.toList());
    }

    private static Stream<String> getLines(String path) {
        Stream<String> lines;
        try {
            lines = Files.lines(Paths.get(path));
            return lines;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    // В квадрат с длиной стороны 1 вписана мишень.
    // Стрелок атакует мишень и каждый раз попадает в произвольную точку квадрата.
    // Надо промоделировать этот процесс с помощью класса java.util.Random и посчитать,
    // какова вероятность попасть в мишень.
    public static double piDividedBy4() {
        Random random = new Random(System.currentTimeMillis());
        final double centerX = 0.5;
        final double centerY = 0.5;
        final double radius = 0.5;

        Supplier<Double> shots = random::nextDouble;

        Stream<Boolean> results = Stream.generate(
                () -> {
                    final double x = shots.get();
                    final double y = shots.get();
                    return (x - centerX) * (x - centerX) + (y - centerY) * (y - centerY) <= radius * radius;
                }
        );

        long result = results.limit(NUMBER_OF_EXPERIMENTS).filter(b -> b).count();
        return (double) result / NUMBER_OF_EXPERIMENTS;
    }

    // Дано отображение из имени автора в список с содержанием его произведений.
    // Надо вычислить, чья общая длина произведений наибольшая.
    public static String findPrinter(Map<String, List<String>> compositions) {
        return compositions.entrySet()
                .stream()
                .max(
                        Comparator.comparing(
                                e -> e.getValue()
                                        .stream()
                                        .collect(
                                                Collectors.summingInt(String::length)
                                        )
                        )
                ).get().getKey();
    }

    // Вы крупный поставщик продуктов. Каждая торговая сеть делает вам заказ в виде Map<Товар, Количество>.
    // Необходимо вычислить, какой товар и в каком количестве надо поставить.
    public static Map<String, Integer> calculateGlobalOrder(List<Map<String, Integer>> orders) {
        return orders
                .stream()
                .flatMap(map -> map.entrySet().stream())
                .collect(
                        Collectors.groupingBy(
                                Map.Entry::getKey,
                                Collectors.collectingAndThen(
                                        Collectors.toList(),
                                        list -> list
                                                .stream()
                                                .mapToInt(Map.Entry::getValue)
                                                .sum()
                                )
                        )
                );
    }
}
