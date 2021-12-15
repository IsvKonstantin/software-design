## Лабораторная 5

Цель: получить практический опыт применения структурного паттерна bridge.

Необходимо реализовать простой визуализатор графов, используя два различных графических API. Способ визуализации графа
можно выбрать самостоятельно (например, рисовать вершины по кругу). Приложение должно поддерживать две реализации
графов: на списках ребер и матрице смежностей. Каркас классов:

```Java
public abstract class Graph {

    /**
     * Bridge to drawing api
     */
    private DrawingApi drawingApi;

    public Graph(DrawingApi drawingApi) {
        this.drawingApi = drawingApi;
    }

    public abstract void drawGraph();
}

public interface DrawingApi {
    long getDrawingAreaWidth();

    long getDrawingAreaHeight();

    void drawCircle(...);

    void drawLine(...);
}
```

Примечания:
* выбор API и реализации графа должны задаваться через аргументы командной строки при запуске приложения
* каркас классов можно менять (добавлять новые поля/методы, параметры методов и тд)
* в качестве drawing api можно использовать java.awt
и javafx ([примеры](https://github.com/akirakozov/software-design/tree/master/java/graphics/))
* можно использовать любой язык и любые api для рисования (главное, чтобы они были принципиально разные)
