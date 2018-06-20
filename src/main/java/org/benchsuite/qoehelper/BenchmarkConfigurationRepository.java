package org.benchsuite.qoehelper;

import org.benchsuite.qoehelper.model.BenchmarkConfiguration;
import org.benchsuite.qoehelper.util.BenchsuiteGitHubRepo;

import java.io.IOException;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BenchmarkConfigurationRepository {

  private static final Logger logger = LoggerFactory.getLogger(BenchsuiteGitHubRepo.class);

  private static Map<String, BenchmarkConfigurationRepository> instances = new HashMap<>();

  public static BenchmarkConfigurationRepository getInstance(String version) throws IOException {

    if(version.equals("DEVELOPMENT")){
      return new BenchmarkConfigurationRepository("master");
    }

    if(version.equals("LATEST")){
      version = BenchsuiteGitHubRepo.getLatestTag();
    }

    if(!instances.containsKey(version)){
      logger.info("Version " + version + " not yet cached. Downloading from GitHub");
      instances.put(version, new BenchmarkConfigurationRepository(version));
    }

    return instances.get(version);
  }

  public static Collection<String> getVersions(){
    List<String> versions = new LinkedList<>();
    versions.addAll(BenchsuiteGitHubRepo.getTags());
    versions.add("DEVELOPMENT");
    versions.add("LATEST");
    return versions;
  }

  private String version;
  private Collection<BenchmarkConfiguration> benchmarks;

  public BenchmarkConfigurationRepository(String version) throws IOException {
    this.version = version;
    this.init();
  }

  private void init() throws IOException {
    this.benchmarks = BenchsuiteGitHubRepo.getRawBenchmarkConfigurations(this.version);
  }

  public Collection<BenchmarkConfiguration> getAllBenchmarkConfigurations() {
    return this.benchmarks;
  }

}
