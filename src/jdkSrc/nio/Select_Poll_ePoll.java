package jdkSrc.nio;

/**
 *  select API :
 * 
 *    int select(int nfds, fd_set *readfds, fd_set *writefds, 
 *          fd_set *exceptfds, struct timeval *timeout); 
 *          
 *      <1> 第一个参数nfds为fdset集合中最大描述符值加1，
 *      <2> fdset是一个位数组，其大小限制为__FD_SETSIZE（1024），位数组的每一位代表其
 *          对应的描述符是否需要被检查。
 *      <3> 第二三四个参数表示需要关注读、写、错误事件的文件描述符位数组，这些参数既是输入
 *          参数也是输出参数，可能会被内核修改用于标示哪些描述符上发生了关注的事件。所以
 *          每次调用select前都需要重新初始化fdset。 
 *      <4> timeout参数为超时时间，该结构会被内核修改，其值为超时剩余的时间。
 *      
 *      <5> select对应于内核中的sys_select调用,sys_select首先将第二三四个参数指向的
 *          fd_set拷贝到内核,然后对每个被SET的描述符调用进行poll,并记录在临时结果中（fdset）,
 *          如果有事件发生，select会将临时结果写到用户空间并返回;当轮询一遍后没有任何事件发生时，
 *          如果指定了超时时间，则select会睡眠到超时，睡眠结束后再进行一次轮询，并将临时结果写到
 *          用户空间，然后返回
 *      <6> select返回后，需要逐一检查关注的描述符是否被SET（事件是否发生）。
 *      
 * -----------------------------------------------------------------------------
 * 
 *  poll API :
 *  
 *    int poll(struct pollfd *fds, nfds_t nfds, int timeout);
 *        
 *       <1> 通过一个pollfd数组向内核传递需要关注的事件,故没有描述符个数的限制
 *       <2> pollfd中的events字段和revents分别用于标示关注的事件和发生的事件，
 *           故pollfd数组只需要被初始化一次。 
 *       <3> poll的实现机制与select类似，其对应内核中的sys_poll，只不过poll向
 *           内核传递pollfd数组，然后对pollfd中的每个描述符进行poll，相比处理
 *           fdset来说，poll效率更高。
 *       <4> poll返回后，需要对pollfd中的每个元素检查其revents值，来得指事件是否发生。
 *       
 * -----------------------------------------------------------------------------
 *  
 *  epoll API : 
 *  
 *    int epoll_wait(int epfd, struct epoll_event *events, int maxevents, int timeout);
 *      
 *      通过 epoll_create     创建一个用于epoll轮询的描述符，
 *      通过 epoll_ctl        添加/修改/删除事件，
 *      通过epoll_wait        检查事件，epoll_wait的第二个参数用于存放结果。 
 *      
 *      epoll与select、poll不同:
 *      <1> 其不用每次调用都向内核拷贝事件描述信息，在第一次调用后，事件信息就会
 *          与对应的epoll描述符关联起来。
        <2> epoll不是通过轮询，而是通过在等待的描述符上注册回调函数，当事件发生时，
                           回调函数负责把发生的事件存储在就绪事件链表中，最后写到用户空间。
 */

public class Select_Poll_ePoll {

}
