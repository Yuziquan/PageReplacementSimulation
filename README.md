## PageReplacementSimulation

[![PageReplacementSimulation](https://img.shields.io/badge/PageReplacementSimulation-v1.0.0-green.svg)](https://github.com/Yuziquan/PageReplacementSimulation)
[![license](https://img.shields.io/packagist/l/doctrine/orm.svg)](https://github.com/Yuziquan/PageReplacementSimulation/blob/master/LICENSE)

> 详情移步: https://blog.csdn.net/wuchangi/article/details/80292685

### 一、项目功能

**模拟实现三种页面置换算法，具体步骤如下：**

<br/>

*设该程序对应的指令的总条数为320。*

#### 1.  基于随机数产生该程序依次执行的指令的地址序列

> 指令地址范围为[0, 319],指令的地址按下述原则生成：
>
> A：50%的指令是顺序执行的
>
> B：25%的指令是均匀分布在前地址部分
>
> C：25%的指令是均匀分布在后地址部分
>
> 
>
> 具体的实施方法是：
>
> A：在[0，319]的指令地址之间随机选取一起点m
>
> B：顺序执行一条指令，即执行地址为m+1 的指令
>
> C：在前地址[0,m+1]中随机选取一条指令并执行，该指令的地址为m'
>
> D：顺序执行一条指令，其地址为m'+1
>
> E：在后地址[m'+2，319]中随机选取一条指令并执行
>
> F：重复步骤A-E，直到320 次指令

<br/>

#### 2. 将指令地址序列根据页面大小转换为页号序列

> 页面大小的取值范围为 1K，2K，4K，8K，16K。
>
> 设页面大小为1K，用户内存容量4页到32页，用户虚存容量为32K。
> 在用户虚存中，按每K(即每页)存放10条指令排列虚存地址，即320条指令在虚存中的存放方式为：
>
> 第 0 条-第 9 条指令为第 0 页（对应虚存地址为[0，9]）;
>
> 第 10 条-第 19 条指令为第 1 页（对应虚存地址为[10，19]）;
>
>  ………………………………
>
> 第 310 条-第 319 条指令为第 31 页（对应虚存地址为[310，319]）;
>
> 
>
> 按以上方式，用户指令可组成 32 页。

<br/>

#### 3. 合并相邻页号

> 在生成的页号序列中，对于相邻相同的页号，合并为一个页号。

<br/>

#### 4. 指定分配给该程序的内存块数

> 分配给该程序的内存块数取值范围为1块，2块，直到程序使用的页面数。

<br/>

#### 5. 执行页面置换算法的模拟过程

> 分别采用 FIFO、LRU和OPT 算法对页号序列进行调度，计算出对应的缺页中断率。
> 并打印出页面大小、分配给程序的内存块数、算法名、对应的缺页中断率。

<br/>

***

### 二、项目运行效果

![1](https://github.com/Yuziquan/PageReplacementSimulation/blob/master/Screenshots/1.png)

***

![2](https://github.com/Yuziquan/PageReplacementSimulation/blob/master/Screenshots/2.png)

***

![3](https://github.com/Yuziquan/PageReplacementSimulation/blob/master/Screenshots/3.png)

***

![](https://github.com/Yuziquan/PageReplacementSimulation/blob/master/Screenshots/4.png)
