/*
 * Class implementing our LazyDupList
 */
public class LazyDupList<T> implements LazyInterface<T> {
	/* TODO: Add the necessary fields and implement all the
	 * methods given in this file to create a Lazy List that
	 * supports duplicate elements
	 */
    Node<T>   head;
     Node<T>  tail;

	LazyDupList() {
        head = new Node(Integer.MIN_VALUE,Integer.MIN_VALUE);
        tail = new Node(Integer.MAX_VALUE,Integer.MAX_VALUE);
        head.next=tail;
	}

	public boolean isEmpty() {
        head.lock();
        tail.lock();
        if(head.next==tail){
            head.unlock();
            tail.unlock();
            return true;
        }
        else{
            head.unlock();
            tail.unlock();
            return false;
        }
	}

	public boolean add(T item) {
        int key =item.hashCode();

        if(key==Integer.MIN_VALUE || key==Integer.MAX_VALUE){
            return false;
        }
        while(true){
            Node<T>  pred=head;
            Node<T>  curr = pred.next;
            while(curr.key <key){
                pred=curr;
                curr=curr.next;
            }
            pred.lock();
            try{
                curr.lock();
                try{
                    if(validate(pred,curr)){

                        Node<T>  node= new Node(item);
                        node.next=curr;
                        pred.next =node;
                        return true;

                    }
                }
                finally{
                    curr.unlock();
                }


            }
            finally{
                pred.unlock();

            }

        }
	}

	public boolean remove(T item) {

        int key =item.hashCode();
        if(key==Integer.MIN_VALUE || key==Integer.MAX_VALUE){
            return false;
        }
        while(true){
            Node<T>  pred=head;
            Node<T>  curr = pred.next;
            while(curr.key <key){
                pred=curr;
                curr=curr.next;
            }
            pred.lock();
            try{
                curr.lock();
                try{
                    if(validate(pred,curr)){
                        if(curr.key!=key){
                            return false;
                        }
                        else{
                            curr.marked=true;
                            pred.next =curr.next;
                            return true;
                        }
                    }
                }
                finally{
                    curr.unlock();
                }


            }
            finally{
                pred.unlock();

            }

        }

	}

	public boolean contains(T item) {
		int key =item.hashCode();
        if(key==Integer.MIN_VALUE || key==Integer.MAX_VALUE){
            return true;
        }
        Node<T>  curr =head;
        while(curr.key<key){
            curr=curr.next;
        }
        return curr.key==key && !curr.marked;
	}

	/* Validate is unique only to the Optimistic and Lazy Lists */
	private boolean validate(Node<T> pred, Node<T> curr) {
		return (!pred.marked && !curr.marked && pred.next==curr);
	}
}
