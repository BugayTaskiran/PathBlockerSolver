import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PathBlocker {

    static final int SIZE = 14;
    static char[][] grid = new char[SIZE][SIZE];
    static boolean[][] visited = new boolean[SIZE][SIZE];
    static int stepCount = 0;

    // 10 levels will be played.
    public static void main(String[] args) throws IOException {
        for (int level = 1; level <= 10; level++) {
            stepCount = 0;
            String levelFilename = String.format("src/level_%d.txt", level);
            String outputFolder = String.format("level%02d", level);

            loadGridFromFile(levelFilename);

            visited = new boolean[SIZE][SIZE];

            dfs(0, 0, outputFolder);

            System.out.printf("Level %d completed, folder %s created.%n", level, outputFolder);
            System.out.println("Continue to the next level...");
        }

        System.out.println("All levels completed successfully.");
    }

    // DFS Algorithm
    public static boolean dfs(int x, int y, String outputFolder) throws IOException {
        if (x < 0 || y < 0 || x >= SIZE || y >= SIZE || grid[x][y] == 'X' || visited[x][y])

        {
            return false;
        }

        if (grid[x][y] == 'T')

        {
            visited[x][y] = true;
            saveGridAsImage(outputFolder);
            return true;
        }

        visited[x][y] = true;
        saveGridAsImage(outputFolder);

        if (dfs(x + 1, y, outputFolder) || dfs(x - 1, y, outputFolder) || dfs(x, y + 1, outputFolder) || dfs(x, y - 1, outputFolder))

        {
            return true;
        }

        visited[x][y] = false;
        return false;
    }

    public static void loadGridFromFile(String filename) throws IOException

    {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        int row = 0;
        while ((line = br.readLine()) != null && row < SIZE)

        {
            grid[row] = line.replaceAll(" ", "").toCharArray();
            row++;
        }
        br.close();
    }

    // draw the steps as png and print to file
    public static void saveGridAsImage(String outputFolder) throws IOException

    {
        int cellSize = 40;
        int imgSize = SIZE * cellSize;
        BufferedImage image = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        for (int i = 0; i < SIZE; i++)
        {
            for (int j = 0; j < SIZE; j++)
            {
                if (grid[i][j] == 'X')

                {
                    g.setColor(Color.BLACK);
                }

                else if (grid[i][j] == 'S')

                {
                    g.setColor(Color.GREEN);
                }

                else if (grid[i][j] == 'T')

                {
                    g.setColor(Color.RED);
                }

                else if (visited[i][j])

                {
                    g.setColor(Color.YELLOW);
                }

                else

                {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                g.setColor(Color.GRAY);
                g.drawRect(j * cellSize, i * cellSize, cellSize, cellSize);
            }
        }
        g.dispose();

        // All levels are creating in file
        File outputfile = new File(String.format("%s/%04d.png", outputFolder, ++stepCount));
        outputfile.getParentFile().mkdirs();
        ImageIO.write(image, "png", outputfile);
    }
}
