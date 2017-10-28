/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zdx.strom.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.BoltDeclarer;
import backtype.storm.topology.SpoutDeclarer;
import backtype.storm.topology.TopologyBuilder;


import com.zdx.strom.example.common.MetaSpout;
import com.alibaba.jstorm.utils.JStormUtils;

/**
 * MonitorTopology
 * 
 * @author longda/zhiyuan.ls
 * 
 */
public class TestTopology {

	private static Logger LOG = Logger.getLogger(TestTopology.class);

	public static String WRITER_COMPONENT = "writer";
	
	private static Map conf = new HashMap<Object, Object>();
	
	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			System.err.println("Please input configuration file");
			System.exit(-1);
		}

		LoadConf(args[0]);

		TopologyBuilder builder = setupBuilder();

		submitTopology(builder);

	}

	private static TopologyBuilder setupBuilder() throws Exception {
		TopologyBuilder builder = new TopologyBuilder();
		int boltParallel = JStormUtils.parseInt(
				conf.get("topology.bolt.parallel"), 1);

		int spoutParallel = JStormUtils.parseInt(
				conf.get("topology.spout.parallel"), 1);
		String spoutName = String.valueOf(conf.get("topology.spout.name"));
		//创建Spout， 其中new MetaSpout() 为真正spout对象，MetaSpout 为spout的名字，注意名字中不要含有空格
		SpoutDeclarer spout = builder.setSpout(spoutName, new MetaSpout(), spoutParallel);
		
		String boltName = String.valueOf(conf.get("topology.bolt.name"));
		//创建bolt, boltName为bolt名字，WriterBolt 为bolt对象，boltParallel为bolt并发数
		//shuffleGrouping(boltName)， 表示接收SspoutName的数据，并且以shuffle方式，
		//即每个spout随机轮询发送tuple到下一级bolt中
		BoltDeclarer totalBolt = builder.setBolt(boltName, new WriterBolt(), boltParallel).shuffleGrouping(spoutName);
		
		//int ackerParal = JStormUtils.parseInt(conf.get("acker.parallel"), 1);
		//Config.setNumAckers(conf, ackerParal);
		//设置表示acker的并发数
		//int workerNum = JStormUtils.parseInt(conf.get("worker.num"), 10);
		//conf.put(Config.TOPOLOGY_WORKERS, workerNum);
		//表示整个topology将使用几个worker
		//conf.put(Config.STORM_CLUSTER_MODE, "distributed");
		//设置topolog模式为分布式，这样topology就可以放到JStorm集群上运行
		
		return builder;
	}

	private static void submitTopology(TopologyBuilder builder) {
		try {
			if (local_mode(conf)) {

				LocalCluster cluster = new LocalCluster();

				cluster.submitTopology(
						String.valueOf(conf.get("topology.name")), conf,
						builder.createTopology());

				Thread.sleep(200000);

				cluster.shutdown();
			} else {
				StormSubmitter.submitTopology(
						String.valueOf(conf.get("topology.name")), conf,
						builder.createTopology());
			}

		} catch (Exception e) {
			LOG.error(e.getMessage(), e.getCause());
		}
	}

	

	private static void LoadProperty(String prop) {
		Properties properties = new Properties();

		try {
			InputStream stream = new FileInputStream(prop);
			properties.load(stream);
		} catch (FileNotFoundException e) {
			System.out.println("No such file " + prop);
		} catch (Exception e1) {
			e1.printStackTrace();

			return;
		}

		conf.putAll(properties);
	}

	private static void LoadYaml(String confPath) {

		Yaml yaml = new Yaml();

		try {
			InputStream stream = new FileInputStream(confPath);

			conf = (Map) yaml.load(stream);
			if (conf == null || conf.isEmpty() == true) {
				throw new RuntimeException("Failed to read config file");
			}

		} catch (FileNotFoundException e) {
			System.out.println("No such file " + confPath);
			throw new RuntimeException("No config file");
		} catch (Exception e1) {
			e1.printStackTrace();
			throw new RuntimeException("Failed to read config file");
		}

		return;
	}

	private static void LoadConf(String arg) {
		if (arg.endsWith("yaml")) {
			LoadYaml(arg);
		} else {
			LoadProperty(arg);
		}
	}

	public static boolean local_mode(Map conf) {
		String mode = (String) conf.get(Config.STORM_CLUSTER_MODE);
		if (mode != null) {
			if (mode.equals("local")) {
				return true;
			}
		}

		return false;

	}

}
