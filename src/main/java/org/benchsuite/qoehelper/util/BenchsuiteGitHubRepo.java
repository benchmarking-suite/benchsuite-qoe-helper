package org.benchsuite.qoehelper.util;

import ca.szc.configparser.Ini;
import ca.szc.configparser.exceptions.IniParserException;
import org.benchsuite.qoehelper.model.BenchmarkConfiguration;
import org.benchsuite.qoehelper.model.BenchmarkConfigurationParsingException;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHTag;
import org.kohsuke.github.GitHub;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BenchsuiteGitHubRepo {

  private static final Logger logger = LoggerFactory.getLogger(BenchsuiteGitHubRepo.class);

  private static final String BENCHSUITE_REPO = "benchmarking-suite/benchsuite-stdlib";
  private static final String BENCHMARKING_FOLDER = "/data/benchmarks";

  private static GHRepository repository;

  static {
    try {
      logger.debug("\"user.home\" is " + System.getProperty("user.home"));
      repository = GitHub.connect().getRepository(BENCHSUITE_REPO);

    } catch (IOException e) {
      logger.warn("Authenticated connection to GitHub failed with " + e.getMessage());
      logger.info("Trying with anonymous connection (limited number of requests per hour)");
      try {
        repository = GitHub.connectAnonymously().getRepository(BENCHSUITE_REPO);
      } catch (IOException e1) {
        logger.error("Error authenticating to GitHub. Cannot contiune: " + e1.getMessage());
        e1.printStackTrace();
      }
    }
  }


  public static String getLatestTag(){
    logger.debug("Asking GitHub for the latest tag created in the repository (corresponding to the newest benchsuite version)...");
    List<String> allTags = getTags();
    String latest = allTags.get(allTags.size()-1);
    logger.debug("Found \"" + latest + "\" as newest tag");
    return latest;
  }

  public static List<String> getTags(){
    logger.debug("Asking GitHub for the list of tags in the repository...");
    List<String> res = new LinkedList<>();
    try {

      res = repository.listTags().asList().stream().sorted((o1, o2) -> {
        try {
          return o1.getCommit().getCommitDate().compareTo(o2.getCommit().getCommitDate());
        } catch (IOException e) {
          e.printStackTrace();
        }
        return 0;
      }).map(GHTag::getName).collect(Collectors.toList());

    } catch (IOException e) {
      e.printStackTrace();
    }
    logger.debug("Following tags have been found: " + res);
    return res;
  }



  public static Collection<BenchmarkConfiguration> getRawBenchmarkConfigurations(String tag) throws BenchmarkConfigurationParsingException {

    logger.info("Fetching benchmark configurations from GitHub for version " + tag);

    Set<BenchmarkConfiguration> res = new HashSet<>();

    try {

      for (GHContent c : repository.getDirectoryContent(BENCHMARKING_FOLDER, tag)) {
        if (c.isFile()) {
          logger.debug("Parsing file " + c.getPath());
          String content = repository.getFileContent(c.getPath(), tag).getContent();
          content = sanitizeContent(content);

          StringReader stringReader = new StringReader(content);
          BufferedReader inFromUser = new BufferedReader(stringReader);
          try {
            Ini ini = new Ini().read(inFromUser);
            Map<String, Map<String, String>> sections = ini.getSections();
            String benchmarkName = c.getName().substring(0, c.getName().length() - 5);
            res.add(new BenchmarkConfiguration(benchmarkName, sections));
          }
          catch (IniParserException ex){
            logger.error("Error parsing ini configuration file " + c.getPath() + ": " + ex.getMessage());
            ex.printStackTrace();
          }
        }
      }

    }
    catch (IOException ex){
      throw new BenchmarkConfigurationParsingException(ex);
    }

    return res;
  }


  private static String sanitizeContent(String content){
    // since config files uses basic interpolation (i.e. the %(key)s format)
    // while this Ini library uses the extended interoplation format (i.e. the ${key} format),
    // before parsing we try to transform from basic to extended interpolation
    // but first we need to escape all the "$" characters
    content = content.replaceAll("\\$", "\\$\\$");
    content = content.replaceAll("%%", "%");
    content = content.replaceAll("%\\(", "\\${DEFAULT:");
    content = content.replaceAll("\\)s", "}");
    return content;
  }
}
