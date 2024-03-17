package com.around.aroundcore;

import com.around.aroundcore.database.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class AroundCoreApplication {

//	private static GameChunkRepository gameChunkRepository;
//	private static GameUserRepository gameUserRepository;
//	private static TeamRepository teamRepository;
//	private static SkillRepository skillRepository;
//	private static GameUserSkillsRepository gameUserSkillsRepository;

//	@Autowired
//	public AroundCoreApplication(
//			GameChunkRepository gameChunkRepository, GameUserRepository gameUserRepository,
//								 TeamRepository teamRepository, SkillRepository skillRepository,
//								 GameUserSkillsRepository gameUserSkillsRepository) {
//		AroundCoreApplication.gameChunkRepository = gameChunkRepository;
//		AroundCoreApplication.gameUserRepository = gameUserRepository;
//		AroundCoreApplication.teamRepository = teamRepository;
//		AroundCoreApplication.skillRepository = skillRepository;
//		AroundCoreApplication.gameUserSkillsRepository = gameUserSkillsRepository;
//	}

	public static void main(String[] args) {
		SpringApplication.run(AroundCoreApplication.class, args);
//		System.out.println(gameChunkRepository.findAll());
//		System.out.println(gameUserRepository.findAll());
//		System.out.println(teamRepository.findAll());
//		System.out.println(skillRepository.findAll());
//		System.out.println(gameUserSkillsRepository.findAll());
	}

}
