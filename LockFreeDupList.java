import java.util.concurrent.atomic.AtomicMarkableReference;

/*
 * Class implementing our LockFreeDupList
 */
public class LockFreeDupList<T>  implements LockFreeInterface<T>  {
	/* TODO: Add the necessary fields and implement all of the methods
	 * in this file to create a Lock-Free List that supports duplicate
	 * elements. Feel free to add whatever helper methods you deem
	 * necessary
	 */
     Node<T>  head;
     Node<T>  tail;




	LockFreeDupList() {
        head = new Node(Integer.MIN_VALUE,Integer.MIN_VALUE);
        tail = new Node(Integer.MAX_VALUE,Integer.MAX_VALUE);
        head.next=new AtomicMarkableReference<Node<T>>(tail, false);
	}

	public boolean isEmpty() {

		if(head.next.getReference()==tail){
            return true;
        }
        return false;
	}

	public boolean add(T item) {
        int key =item.hashCode();
        if(key==Integer.MIN_VALUE || key==Integer.MAX_VALUE){
            return false;
        }

        while(true){
            Window window= new Window(head,tail);
            window=window.find(head,key);
            Node<T> pred =window.pred, curr=window.curr;
            Node<T> node = new Node(item);
            node.next = new AtomicMarkableReference(curr,false);
            if(pred.next.compareAndSet(curr,node,false,false)){
                return true;
            }
        }
	}

	public boolean remove(T item) {

		int key = item.hashCode();
        if(key==Integer.MIN_VALUE || key==Integer.MAX_VALUE){
            return false;
        }
        boolean snip;
        boolean test;
        while(true){
            Window window= new Window(head,tail);
            window=window.find(head,key);
            Node<T> pred =window.pred, curr=window.curr;
            if(curr.key !=key){

                return false;
            }
            else{

                Node<T> succ =curr.next.getReference();
                snip=curr.next.compareAndSet(succ,succ,false,true);

                if(!snip){
                    continue;
                }
                test=pred.next.compareAndSet(curr,succ,false,false);
                if(test==true){

                }
                else{

                }
                return true;

            }

        }
	}

	public boolean contains(T item) {

        int key =item.hashCode();
        if(key==Integer.MIN_VALUE || key==Integer.MAX_VALUE){
            return true;
        }
        boolean[] marked ={false};
        Node<T> curr=head;
        while(curr.key<key){
            curr=curr.next.getReference();
            Node<T> succ= curr.next.get(marked);
        }
        return (curr.key==key  && !marked[0]);
	}






}
