package com.leongregori.cmdbclient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

import static org.junit.jupiter.api.Assumptions.*;

@SpringBootTest
class CmdbGitInterfaceTest {

	@Value("${git.token}")
	private String gitToken;
	@Value("${git.cmdb.url}")
	private String gitUrl;

	@Test
	void checkConfigurationNotEmpty() {
		CmdbGitInterface cmdb = new CmdbGitInterface(gitUrl, gitToken, "master", "wrongUrlTail");
		HashMap configuration = cmdb.getCmdb();
		assumeFalse(configuration.isEmpty());
	}
}