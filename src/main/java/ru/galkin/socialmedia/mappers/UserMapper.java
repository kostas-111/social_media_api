package ru.galkin.socialmedia.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.galkin.socialmedia.dto.UserDto;
import ru.galkin.socialmedia.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mapping(target="id", source="id")
  @Mapping(target="name", source="name")
  @Mapping(target="email", source="email")
  @Mapping(target="password", source="password")
  UserDto getDtoFromEntity(User user);

  @InheritInverseConfiguration
  User getEntityFromDto(UserDto userDto);
}
