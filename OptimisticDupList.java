/*
 * Class implementing our OptimisticDuplLists
 */
public class OptimisticDupList<T> implements OptimisticInterface<T> {
	/* TODO: Add the necessary fields and implement all the methods given
	 * in this file to create an Optimistic List that supports duplicate
	 * elements
	 */
     Node<T> head;
     Node<T>  tail;


	OptimisticDupList() {
		head = new Node(Integer.MIN_VALUE,Integer.MIN_VALUE);
        tail = new Node(Integer.MAX_VALUE,Integer.MAX_VALUE);
        head.next=tail;

	}

	public boolean isEmpty() {
		//throw new UnsupportedOperationException();
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
            while(curr.key <=key){
                pred=curr;
                curr=curr.next;
            }
            pred.lock();
            curr.lock();
            try{
                if(validate(pred,curr)){
                    Node<T> node =new Node(item);
                    node.next=curr;
                    pred.next=node;
                    return true;
                }


            }finally{
                pred.unlock();
                curr.unlock();
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
            curr.lock();
            try{
                if(validate(pred,curr)){
                    if(curr.key==key){
                        pred.next=curr.next;
                        return true;
                    }
                    else{
                        return false;
                    }
                }

            }finally{
                pred.unlock();
                curr.unlock();
            }

        }
	}

	public boolean contains(T item) {

        int key =item.hashCode();
        if(key==Integer.MIN_VALUE || key==Integer.MAX_VALUE){
            return true;
        }



        while(true){

            Node<T>  pred = this.head;
            Node<T> curr = pred.next;

            while(curr.key<key){
                pred=curr;
                curr=curr.next;
            }
                try{
                    pred.lock();
                    curr.lock();
                    if(validate(pred,curr)){


                        return (curr.key==key);
                    }

                }finally{
                    pred.unlock();
                    curr.unlock();
                }




        }

	}

	/* Validate is unique only to the Optimistic and Lazy Lists */
	public boolean validate(Node<T> pred, Node<T> curr) {
		Node<T>  node =head;
        while(node.key<=pred.key){
            if(node==pred){
                return(pred.next==curr);
            }
            node=node.next;

        }
        return false;
	}

}
