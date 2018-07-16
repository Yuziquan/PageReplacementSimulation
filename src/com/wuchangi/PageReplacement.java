package com.wuchangi;

/*
 * @program: PageReplacementSimulation
 * @description: PageReplacement
 * @author: WuchangI
 * @create: 2018-06-07-19-29
 **/

import java.util.*;

public class PageReplacement
{
    //页面大小,每个页面可包含instructionsNumPerPage条指令
    public static int instructionsNumPerPage;

    //存放该程序依次执行的指令的有序地址序列
    public static int[] instructionsSequence = null;

    //存放将有序指令地址序列转换成(经过合并相邻页号)的有序页号序列
    public static int[] pagesSequence = null;

    //指定分配给该程序的内存块数
    public static int memoryBlocksNum;


    public static void main(String[] args)
    {

        int count = 1;
        Scanner scan = new Scanner(System.in);

        System.out.println("\t\t**********欢迎使用页面置换模拟系统！**********\n");

        while (true)
        {
            System.out.println("*****第 " + count + " 个程序的页面置换模拟过程*****\n");
            System.out.println("请输入程序包含的指令条数：（只支持5的倍数, 退出系统请输入-1)");

            int inputValue = scan.nextInt();

            if(inputValue == -1) break;

            int instructionsNum = inputValue;

            instructionsSequence = generateInstructionsSequence(instructionsNum);
            System.out.println("系统随机生成的指令地址序列如下：");
            showInstructionsSequence(instructionsSequence);
            System.out.println();

            System.out.println("请输入页面大小（1,2,4,8,16 分别表示 1k,2k,4k,8k,16k）：");

            //每1k存放10条指令
            instructionsNumPerPage = scan.nextInt() * 10;
            pagesSequence = convertToPagesSequence(instructionsSequence, instructionsNumPerPage);
            System.out.println("该指令地址序列对应的页号序列（已经过相邻页号合并）如下：");
            showPagesSequence(pagesSequence);
            System.out.println();
            System.out.println("实际总共使用到的页号个数为：" + pagesSequence.length);
            System.out.println();

            System.out.println("请输入分配给该程序的内存块数:（1~" + pagesSequence.length + ")");
            memoryBlocksNum = scan.nextInt();

            while(true)
            {
                System.out.println("请输入需要模拟的页面置换算法标号：（1：FIFO， 2：LRU， 3：OPT, 退出该程序的页面置换模拟过程请输入-1)");
                int flag = scan.nextInt();

                if(flag == -1) break;

                switch (flag)
                {
                    case 1:
                        FIFO(pagesSequence, memoryBlocksNum);
                        break;
                    case 2:
                        LRU(pagesSequence, memoryBlocksNum);
                        break;
                    case 3:
                        OPT(pagesSequence, memoryBlocksNum);
                        break;
                    default:
                        System.out.println("您的输入有误！");
                }

                System.out.println();
            }

            System.out.println("\n\n");

            count++;
        }

        System.out.println("\n~~~~~~~~~~您已成功退出系统！~~~~~~~~~~");

    }

    //instructionsNum为5的倍数
    public static int[] generateInstructionsSequence(int instructionsNum)
    {
        int[] instructionsSequence = new int[instructionsNum];

        int count = 0;

        while (count < instructionsNum)
        {
            int randomAddress1 = 0 + (int) (Math.random() * (((instructionsNum - 1) - 0) + 1));
            instructionsSequence[count] = randomAddress1;
            randomAddress1++;
            instructionsSequence[++count] = randomAddress1;

            int randomAddress2 = 0 + (int) (Math.random() * ((randomAddress1 - 0) + 1));
            instructionsSequence[++count] = randomAddress2;
            randomAddress2++;
            instructionsSequence[++count] = randomAddress2;

            int randomAddress3 = (randomAddress2 + 1) + (int) (Math.random() * (((instructionsNum - 1) - (randomAddress2 + 1)) + 1));
            instructionsSequence[++count] = randomAddress3;

            count++;
        }

        return instructionsSequence;
    }

    public static void showInstructionsSequence(int[] instructionsSequence)
    {
        for (int i = 0; i < instructionsSequence.length; i++)
        {
            System.out.printf("%5s", instructionsSequence[i]);

            if ((i + 1) % 20 == 0)
            {
                System.out.println();
            }
        }

        System.out.println();
    }


    public static int[] convertToPagesSequence(int[] instructionsSequence, int instructionsNumPerPage)
    {
        ArrayList<Integer> pagesList = new ArrayList<Integer>();

        int temp = -1;
        //页号
        int pageIndex;

        for (int i = 0; i < instructionsSequence.length; i++)
        {
            pageIndex = instructionsSequence[i] / instructionsNumPerPage;

            //将相邻的页号合并
            if (pageIndex != temp)
            {
                pagesList.add(pageIndex);
                temp = pageIndex;
            }
        }

        //有序页号序列经合并之后长度最长不超过指令的有序地址序列长度
        int[] pagesSequence = new int[pagesList.size()];

        for (int i = 0; i < pagesList.size(); i++)
        {
            pagesSequence[i] = pagesList.get(i);
        }

        return pagesSequence;
    }


    public static void showPagesSequence(int[] pagesSequence)
    {
        for (int i = 0; i < pagesSequence.length; i++)
        {
            System.out.printf("%5s", pagesSequence[i]);

            if ((i + 1) % 20 == 0)
            {
                System.out.println();
            }
        }

        System.out.println();
    }


    public static void FIFO(int[] pagesSequence, int memoryBlocksNum)
    {
        //执行页号序列期间内存块的状态
        int[][] memoryBlocksState = new int[pagesSequence.length][memoryBlocksNum];

        //该指针指向将要被置换的内存块的位置（下标位置）
        int curPosition = 0;

        //执行每个页号时内存块序列的状态
        int[] tempState = new int[memoryBlocksNum];

        //记录缺页情况， 1表示缺页，0表示不缺页
        int[] isLackOfPage = new int[pagesSequence.length];
        Arrays.fill(isLackOfPage, 0, pagesSequence.length, 0);

        //缺页次数
        int lackTimes = 0;

        //开始时，内存块状态都为空闲（-1表示）
        Arrays.fill(tempState, 0, memoryBlocksNum, -1);

        for (int i = 0; i < pagesSequence.length; i++)
        {
            //如果缺页
            if (findKey(tempState, 0, memoryBlocksNum - 1, pagesSequence[i]) == -1)
            {
                isLackOfPage[i] = 1;
                lackTimes++;
                tempState[curPosition] = pagesSequence[i];

                //指针向右移动超过memoryBlocksNum时，重置其指向开始的内存块位置0
                if (curPosition + 1 > memoryBlocksNum - 1)
                {
                    curPosition = 0;
                }
                else
                {
                    curPosition++;
                }
            }

            //保存当前内存块序列的状态
            System.arraycopy(tempState, 0, memoryBlocksState[i], 0, memoryBlocksNum);

        }

        showMemoryBlocksState(memoryBlocksState, pagesSequence, memoryBlocksNum, isLackOfPage, lackTimes);
    }

    public static void LRU(int[] pagesSequence, int memoryBlocksNum)
    {
        //维护一个最近使用的内存块集合
        LRULinkedHashMap<String, Integer> recentVisitedBlocks = new LRULinkedHashMap<String, Integer>(memoryBlocksNum);

        //执行页号序列期间内存块的状态
        int[][] memoryBlocksState = new int[pagesSequence.length][memoryBlocksNum];

        //该指针指向将要被置换的内存块的位置（下标位置）
        int curPosition = 0;

        //执行每个页号时内存块序列的状态
        int[] tempState = new int[memoryBlocksNum];

        //记录缺页情况， 1表示缺页，0表示不缺页
        int[] isLackOfPage = new int[pagesSequence.length];
        Arrays.fill(isLackOfPage, 0, pagesSequence.length, 0);

        //缺页次数
        int lackTimes = 0;

        //开始时，内存块状态都为空闲（-1表示）
        Arrays.fill(tempState, 0, memoryBlocksNum, -1);

        for (int i = 0; i < pagesSequence.length; i++)
        {
            //如果缺页
            if(findKey(tempState, 0, memoryBlocksNum - 1, pagesSequence[i]) == -1)
            {
                isLackOfPage[i] = 1;
                lackTimes++;

                //如果内存块还有剩余
                if(tempState[memoryBlocksNum - 1] == -1)
                {
                    tempState[curPosition] = pagesSequence[i];
                    recentVisitedBlocks.put(String.valueOf(pagesSequence[i]), pagesSequence[i]);
                    curPosition++;
                }
                //如果内存块都已被使用
                else
                {
                    //找到当前内存块序列中最近最少使用的内存块，并将其置换
                    curPosition = findKey(tempState, 0, memoryBlocksNum - 1, recentVisitedBlocks.getHead());
                    tempState[curPosition] = pagesSequence[i];
                    recentVisitedBlocks.put(String.valueOf(pagesSequence[i]), pagesSequence[i]);
                }
            }
            //如果不缺页
            else
            {
                //将这里被使用的pageSequence[i]在最近使用的内存块集合中的原先位置调整到最近被访问的位置
                recentVisitedBlocks.get(String.valueOf(pagesSequence[i]));
            }

            //保存当前内存块序列的状态
            System.arraycopy(tempState, 0, memoryBlocksState[i], 0, memoryBlocksNum);
        }

        showMemoryBlocksState(memoryBlocksState, pagesSequence, memoryBlocksNum, isLackOfPage, lackTimes);
    }

    public static void OPT(int[] pagesSequence, int memoryBlocksNum)
    {
        //执行页号序列期间内存块的状态
        int[][] memoryBlocksState = new int[pagesSequence.length][memoryBlocksNum];

        //该指针指向将要被置换的内存块的位置（下标位置）
        int curPosition = 0;

        //执行每个页号时内存块序列的状态
        int[] tempState = new int[memoryBlocksNum];

        //记录缺页情况， 1表示缺页，0表示不缺页
        int[] isLackOfPage = new int[pagesSequence.length];
        Arrays.fill(isLackOfPage, 0, pagesSequence.length, 0);

        //缺页次数
        int lackTimes = 0;

        //开始时，内存块状态都为空闲（-1表示）
        Arrays.fill(tempState, 0, memoryBlocksNum, -1);

        for (int i = 0; i < pagesSequence.length; i++)
        {
            //如果缺页
            if(findKey(tempState, 0, memoryBlocksNum - 1, pagesSequence[i]) == -1)
            {
                isLackOfPage[i] = 1;
                lackTimes++;

                //如果内存块还有剩余
                if(tempState[memoryBlocksNum - 1] == -1)
                {
                    tempState[curPosition] = pagesSequence[i];
                    curPosition++;
                }
                //如果内存块都已被使用
                else
                {
                    int maxLoc = 0;

                    for(int j = 0; j < memoryBlocksNum; j++)
                    {
                        //找出当前内存块序列中的内存块tempState[j]在将来会被访问到的（第一个）位置
                        int loc = findKey(pagesSequence, i + 1, pagesSequence.length - 1, tempState[j]);

                        //如果将来该内存块都不再被使用了
                        if (loc == -1)
                        {
                            curPosition = j;
                            break;
                        }
                        //找出当前内存块序列中的所有内存块在将来会被访问到的最远位置，设为maxLoc
                        else
                        {
                            if(maxLoc < loc)
                            {
                                maxLoc = loc;
                                curPosition = j;
                            }
                        }
                    }

                    tempState[curPosition] = pagesSequence[i];
                }
            }

            //保存当前内存块序列的状态
            System.arraycopy(tempState, 0, memoryBlocksState[i], 0, memoryBlocksNum);
        }

        showMemoryBlocksState(memoryBlocksState, pagesSequence, memoryBlocksNum, isLackOfPage, lackTimes);

    }


    //返回key在arr中第一次出现的位置,start和end为数组下标, 找不到则返回-1
    public static int findKey(int[] arr, int start, int end, int key)
    {
        for (int i = start; i <= end; i++)
        {
            if (arr[i] == key)
            {
                return i;
            }
        }

        return -1;
    }


    public static void showMemoryBlocksState(int[][] memoryBlocksState, int[] pagesSequence, int memoryBlocksNum, int[] isLackofPage, int lackTimes)
    {
        String[] pagesDescription = {"不缺页", "缺页"};

        int pagesSequenceLength = pagesSequence.length;

        for (int i = 0; i < pagesSequenceLength; i++)
        {
            System.out.println("当前访问页号：" + pagesSequence[i]);
            System.out.print("\t");

            for (int j = 0; j < memoryBlocksNum * 6 + 1; j++)
            {
                System.out.print("-");
            }

            System.out.print("\n\t");

            for (int k = 0; k < memoryBlocksNum; k++)
            {
                if (k == 0)
                {
                    System.out.print("|");
                }
                //如果当前内存块还没被使用，置为空
                if (memoryBlocksState[i][k] == -1)
                {
                    System.out.printf("%5s|", " ");
                }
                else
                {
                    System.out.printf("%5s|", memoryBlocksState[i][k]);
                }
            }

            System.out.print("  缺页情况：" + pagesDescription[isLackofPage[i]]);

            System.out.print("\n\t");

            for (int j = 0; j < memoryBlocksNum * 6 + 1; j++)
            {
                System.out.print("-");
            }

            System.out.println();
        }

        //缺页率
        double lackOfPagesRate = lackTimes * 1.0 / pagesSequence.length;

        System.out.println("\n该程序的页号序列长度为：" + pagesSequence.length + ", 执行该算法后，缺页次数为：" + lackTimes + ", 缺页率为：" + lackOfPagesRate * 100 + "%");
    }

}



//LRU算法的辅助存储类
class LRULinkedHashMap<K, V> extends LinkedHashMap<K, V>
{
    //最大内存块数（容量）
    private int maxMemoryBlocksNum;

    //设置默认负载因子
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    public LRULinkedHashMap(int maxCapacity)
    {
        //设置accessOrder为true，保证了LinkedHashMap底层实现的双向链表是按照访问的先后顺序排序
        super(maxCapacity, DEFAULT_LOAD_FACTOR, true);
        this.maxMemoryBlocksNum = maxCapacity;
    }

    //得到最近最少被访问的元素
    public V getHead()
    {
        return (V) this.values().toArray()[0];
    }

    //移除多余的最近最少被访问的元素
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest)
    {
        return size() > maxMemoryBlocksNum;
    }

}