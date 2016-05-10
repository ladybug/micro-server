package com.aol.micro.server.reactive;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import com.aol.cyclops.control.Eval;
import com.aol.cyclops.control.Maybe;
import com.aol.cyclops.control.ReactiveSeq;
import com.aol.cyclops.data.async.QueueFactories;
import com.aol.cyclops.data.collections.extensions.standard.ListX;
import com.aol.cyclops.data.collections.extensions.standard.QueueX;
import com.aol.cyclops.data.collections.extensions.standard.SetX;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class EventQueueManagerTest {
	
	@Test
	public void streamMap(){
		
		Stream.of(1)
			  .map(i->i*2);
		
		//Stream[2]
		
		
		Optional.of(1)
				.map(i->i*2);
		
		//Optional[2]              
		
		
		
		
		
		CompletableFuture.completedFuture(1)
						 .thenApply(i->i*2);
		
		//CompletableFuture[2]                             
		
		
		Arrays.asList(1)
			  .stream()
		      .map(i->i*2)
		      .collect(Collectors.toList());
	
		//ArrayList[2]
		
		
		ListX.of(1,2)
			 .map(i->i*2);
		
		//ListX[2,4]
		
		
		SetX.of(10,20,30,40,50)
			.grouped(2)
			.printOut();
		
		
		
		
	
		QueueX.of(1,2,3,4,5)
		      .schedule("0 20 * * *",Executors.newScheduledThreadPool(1))
		      .connect()
		      .map(this::processJob)
		      .map(status->"Job complete : " + status)
		      .printOut();
		
		
		
		
		
	}
	private String processJob(int in){
		return null;
	}
	EventQueueManager<String> manager;
	Executor ex = Executors.newFixedThreadPool(10);
	volatile String recieved;
	@Before
	public void setup(){
		recieved =null;
		manager = EventQueueManager.of(ex,QueueFactories.boundedNonBlockingQueue(1000));
	}
	

	@Test
	public void testPush() {
		manager.push("hello", "world");
	}

	public void handleEvent(String e){
		
	}
	@Test
	public void testConfigure() throws InterruptedException {
		String data = "";
		manager.forEach("bus-a",this::handleEvent);
		
		manager.push("bus-a", data);
		
	
		manager.forEach("hello",a->recieved= a);
		
		manager.push("hello", "world");
		
		Thread.sleep(100);
		
		System.out.println(recieved);
		assertThat(recieved,equalTo("world"));
	}
	public String process(String s){
		return null;
	}
	@Test
	public void testStream() throws InterruptedException {
		
	
		
		manager.stream("2")
		        .futureOperations(ex)
		        .forEach(a->recieved= a);
		
		manager.push("2", "world");
		
		
		Thread.sleep(100);
		
		System.out.println(recieved);
		assertThat(recieved,equalTo("world"));
	}

	@Test
	public void testLazyValue() {
		
		ReactiveSeq.generate(()->"input")
					.onePer(1,TimeUnit.SECONDS)
					.futureOperations(ex)
					.forEach(n->manager.push("lazy",n));
					
		Eval<String> lazy = manager.lazy("lazy");
		
		lazy = lazy.map(in->in+"-message!")
				   .map(in->in+"!");
		
		
		
		
		
		assertThat(lazy.get(),equalTo("input-message!!"));
		assertThat(lazy.get(),equalTo("input-message!!"));
			 
       
	}
	AtomicInteger  count =new AtomicInteger(0);
	@Test
	public void testMaybe() {
		
		ReactiveSeq.generate(()->"input")
					.onePer(1,TimeUnit.SECONDS)
					.map(s->s+":"+count.incrementAndGet())
					.peek(System.out::println)
					.futureOperations(ex)
					.forEach(n->manager.push("lazy",n));
					
		Maybe<String> lazy1 = manager.maybe("lazy");
		Maybe<String> lazy2 = manager.maybe("lazy");
		
		
		
		
		
		assertThat(lazy1.get(),anyOf(equalTo("input:1"),equalTo("input:2")));
		assertThat(lazy2.get(),anyOf(equalTo("input:1"),equalTo("input:2"),equalTo("input:3")));
			 
       
	}
	
	public String restCall(String in){
		return "hello";
	}

	@Test
	public void testIoFutureStream() throws InterruptedException {
		
		
		
		
		manager.ioFutureStream("futureStream")
        	   .peek(a->recieved= a)
        	   .run();

		
		manager.push("futureStream", "world");

		Thread.sleep(100);

		System.out.println(recieved);
		assertThat(recieved,equalTo("world"));
	}

}
