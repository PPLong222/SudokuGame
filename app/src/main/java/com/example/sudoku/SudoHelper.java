package com.example.sudoku;

import android.util.Log;

import java.util.Random;

public class SudoHelper {
    public static final int LEVEL_NORMAL = 2;
    public static final int LEVLE_EASY = 1;
    public static final int LEVLE_DIF = 3;
    public static final int BLOCK_ISORIGIN=-1;
    public static final int BLOCK_NOTOORIGIN=0;
    public static  int[][] testNum={
            {5, 3, 4, 6, 7, 8, 9, 1, 2},
            {6, 7, 2, 1, 9, 5, 3, 4, 8},
            {1, 9, 8, 3, 4, 2, 5, 6, 7},
            {8, 5, 9, 0, 6, 1, 4, 2, 3},
            {4, 2, 6, 8, 5, 3, 0, 0, 1},
            {7, 1, 3, 9, 2, 4, 8, 5, 6},
            {9, 6, 1, 5, 3, 7, 2, 8, 4},
            {2, 8, 7, 4, 1, 9, 6, 3, 5},
            {3, 4, 5, 2, 8, 6, 1, 7, 9}
    };

    public static boolean solveSudoku(int[][] board,int num) {

        return backTrace(board, 0, 0,num);
    }
    public static boolean solveSudoku_noChange(int[][] board,int num) {
        int temp[][]=new int[9][9];
        for(int i=0;i<9;i++){
            System.arraycopy(board[i],0,temp[i],0,9);
        }
        return backTrace(temp, 0, 0,num);
    }

    //注意这里的参数，row表示第几行，col表示第几列。
    public static boolean backTrace(int[][] board, int row, int col,int num) {
        //row从0开始的，当row等于board.length的时候表示数独的最后一行全部读遍历完了，说明数独中的值是有效的，直接返回true
        if (row == board.length)
            return true;
        //如果当前行的最后一列也遍历完了，就从下一行的第一列开始。这里的遍历
        //顺序是从第1行的第1列一直到最后一列，然后第二行的第一列一直到最后
        if (col == board.length)
            return backTrace(board, row + 1, 0,num);
        //如果当前位置已经有数字了，就不能再填了，直接到这一行的下一列
        if (board[row][col] != 0)
            return backTrace(board, row, col + 1,num);
        //如果上面条件都不满足，我们就从1到9种选择一个合适的数字填入到数独中
        for (char i = 1; i <= 9; i++) {
            //判断当前位置[row，col]是否可以放数字i，如果不能放再判断下
            //一个能不能放，直到找到能放的为止，如果从1-9都不能放，就会下面
            //直接return false
            if(i==num)
                continue;
            if (!isValid(board, row, col, i))
                continue;
            //如果能放数字i，就把数字i放进去
            board[row][col] = i;
            //如果成功就直接返回，不需要再尝试了
            if (backTrace(board, row, col,num))
                return true;
            //否则就撤销重新选择
            board[row][col] = 0;
        }
        //如果当前位置[row，col]不能放任何数字，直接返回false
        return false;
    }

    //判断是否该行该列放这个数字是否会破坏规则
    public static boolean isValid(int[][] board, int row, int col, int c) {
        for (int i = 0; i < 9; i++) {
            //当前列有没有和字符c重复的
            if (board[i][col] == c)
                return false;
            //当前行有没有和字符c重复的
            if (board[row][i] == c)
                return false;
            //当前的单元格内是否有和字符c重复的
            if (board[3 * (row / 3) + i / 3][3 * (col / 3) + i % 3] == c)
                return false;
        }
        return true;
    }

    public static int[][] generateSudoWithLevel(int level){
        int temp[][]=new int[9][9];
        int origin[][]=new int[9][9];


        Random rand=new Random();
        int count=0;

        switch (level){
            case LEVEL_NORMAL:
                count = 25;
                break;
            case LEVLE_EASY:
                count = 20;
                break;
            case LEVLE_DIF:
                count = 35;
                break;
        }
        int calloc_num = 30;
        int count_fresh = 0;
        boolean flag = true;
        do {
            while (true) {
                while (calloc_num > 0) {
                    int i = rand.nextInt(9);
                    int j = rand.nextInt(9);
                    if (temp[i][j] == 0) {
                        int num = rand.nextInt(9) + 1;
                        if (isValid(temp, i, j, num)) {
                            temp[i][j] = num;
                            calloc_num--;
                        }

                    }
                }

                for (int i = 0; i < 9; i++) {
                    System.arraycopy(temp[i], 0, origin[i], 0, 9);
                }


                if (solveSudoku(temp, 0)) break;
                calloc_num = 30;
                temp = new int[9][9];
                origin = new int[9][9];
            }


            for (int i = 0; i < count + 1; i++) {
                int r = rand.nextInt(9);
                int c = rand.nextInt(9);
                if (i == count && count_fresh <= 20) {
                    return temp;
                }
                if (temp[r][c] != 0 && isOnlyOne(r, c, temp)) {

                    continue;
                }
                count_fresh++;
                Log.d("1111","resh"+count_fresh);
                if (count_fresh > 20) {
                    flag = true;
                    break;
                }
                i--;

            }

            count_fresh = 0;
            calloc_num = 30;
            temp = new int[9][9];
            origin = new int[9][9];
            Log.d("1111","rebuild !");
        }while (flag) ;
            return null;
        }



    public static boolean isOnlyOne(int i,int j,int[][] b){  //判断在i,j挖去数字后是否有唯一解
        int k=b[i][j];  //待挖洞的原始数字
        for(int num=1;num<10;num++){
            //b[i][j]=num;
            b[i][j]=0;
            if(num!=k&&solveSudoku_noChange(b,k)){     //除待挖的数字之外，还有其他的解，则返回false
                for(int n=0;n<9;n++){
                    for(int m=0;m<9;m++){
                        System.out.print(b[n][m]+" ");
                    }
                    System.out.println();
                }
                b[i][j]=k;

                return false;
            }
        }

        return true;     //只有唯一解则返回true
    }
}
