package com.mcaprep.domain.mapper

import com.mcaprep.data.local.entities.ProjectEntity
import com.mcaprep.data.local.entities.UserDetailsEntity
import com.mcaprep.data.local.entities.UserEntity
import com.mcaprep.data.remote.model.ProjectDto
import com.mcaprep.data.remote.model.UserDto
import com.mcaprep.data.remote.model.UserPersonalDetailsDto
import com.mcaprep.domain.model.User
import com.mcaprep.domain.model.UserProject


fun UserEntity.toDomain(projects: List<ProjectEntity>) = User(
    id = id,
    name = name,
    email = email,
    phone = userDetails.phoneNumber,
    role = role,
    loginMethod = login,
    college = userDetails.college,
    plan = plan,
    startDate = startDate,
    duration = duration,
    streak = streak,
    lastActiveDate = lastDate,
    examYear = userDetails.examYear,
    dob = userDetails.dob,
    projects = projects.map {
        UserProject(
            id = it.id,
            projectId = it.projectId,
            paymentId = it.paymentId,
            purchaseDate = it.purchaseDate
        )
    }
)

fun UserDto.toDomain(): User {
    return User(
        id = id,
        name = name,
        email = email,
        phone = userDetails.phoneNumber,
        role = role,
        loginMethod = login,
        college = userDetails.college,
        plan = plan,
        startDate = startDate,
        duration = duration,
        streak = streak,
        lastActiveDate = lastDate,
        examYear = userDetails.examYear,
        dob = userDetails.dob,
        projects = project.map { it.toDomain() }
    )
}

fun ProjectDto.toDomain(): UserProject {
    return UserProject(
        id = id,
        projectId = projectId,
        paymentId = paymentId,
        purchaseDate = purchaseDate
    )
}

fun UserDto.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        name = name,
        email = email,
        login = login,
        role = role,
        createdAt = createdAt,
        updatedAt = updatedAt,
        lastDate = lastDate,
        streak = streak,
        college = userDetails.college,
        date = date,
        number = number,
        plan = plan,
        startDate = startDate,
        duration = duration,
        userDetails = userDetails.toEntity()
    )
}

fun ProjectDto.toEntity(userId: String): ProjectEntity {
    return ProjectEntity(
        id = id,
        projectId = projectId,
        paymentId = paymentId,
        purchaseDate = purchaseDate,
        userOwnerId = userId
    )
}

fun UserPersonalDetailsDto.toEntity(): UserDetailsEntity {
    return UserDetailsEntity(
        phoneNumber = phoneNumber,
        dob = dob,
        college = college,
        examYear = examYear
    )
}

