

public class Window<T> extends LockFreeDupList<T>{
    public Node<T> pred, curr;
    Window(Node<T> myPred, Node<T> myCurr){
        pred=myPred; curr=myCurr;
    }
    public Window find(Node<T> head, int key){


        Node<T>  pred =null, curr=null, succ=null;
        boolean[] marked= {false};
        boolean snip;
        retry:while(true ){
            pred=head;
            curr=pred.next.getReference();
            while(true){
                succ =curr.next.get(marked);


                while(marked[0]){

                    snip =pred.next.compareAndSet(curr,succ,false,false);

                    if(!snip){

                        continue retry;
                    }

    
                    curr =succ;
                    succ=curr.next.get(marked);
                }
                if(curr.key>=key){

                    return new Window(pred,curr);

                }
                pred=curr;
                curr=succ;

            }
        }
    }

}
