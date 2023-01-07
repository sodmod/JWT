package com.Security.JWT.Cotrollers;

import com.Security.JWT.Exception.TokenRefreshException;
import com.Security.JWT.Payload.Request.TokenRefreshRequest;
import com.Security.JWT.Payload.Response.JwtResponse;
import com.Security.JWT.Payload.Response.MessageResponse;
import com.Security.JWT.Payload.Response.TokenRefreshResponse;
import com.Security.JWT.Repository.RoleRepository;
import com.Security.JWT.Repository.UserRepository;
import com.Security.JWT.Payload.Request.LoginRequest;
import com.Security.JWT.Payload.Request.SignupRequest;
import com.Security.JWT.Security.Services.RefreshTokenService;
import com.Security.JWT.Security.Services.UserDetailsImpl;
import com.Security.JWT.Security.jwt.JwtUtils;
import com.Security.JWT.models.ERole;
import com.Security.JWT.models.RefreshToken;
import com.Security.JWT.models.Role;
import com.Security.JWT.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    RefreshTokenService refreshTokenService;

    @PostMapping("/signIn")
    public ResponseEntity<?> authenticateUser(@Validated @RequestBody LoginRequest loginRequest){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);


        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());


        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok(
                new JwtResponse(
                        jwt,
                        refreshToken.getToken(),
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles
                )
        );
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@Validated @RequestBody TokenRefreshRequest request){
//        This expression below will get the refreshToken from the request header and store it as a string variable 'requestRefreshToken'
        String requestRefreshToken = request.getRefreshToken();

        //ToDo RefreshToken conncecting to database


        return refreshTokenService.
                findByToken(
                        requestRefreshToken
                )
                .map(
                        refreshTokenService::verifyExpiration
                )
                .map(
                        RefreshToken::getUser
                )
                .map(
                        user-> {
                            String token = jwtUtils.generateTokenFromUsername(
                                    user.getUsername()
                            );
                            return ResponseEntity.ok(
                                    new TokenRefreshResponse(
                                            token,
                                            requestRefreshToken
                                    )
                            );
                        }
                        )
                .orElseThrow(
                        ()-> new TokenRefreshException(
                                requestRefreshToken,
                                "Refresh token is not in database"
                        )
                );
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Validated @RequestBody SignupRequest signupRequest){

        if (userRepository.existsByEmail(signupRequest.getUsername())){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already is already taken!", 0));
        }

        if(userRepository.existsByEmail(signupRequest.getEmail())){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!", 0));
        }

//        Create new user's account

        User user = new User(signupRequest.getUsername(),
                signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword()
                )
        );

        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null){
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role ->{
                switch (role){
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;

                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;

                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return  ResponseEntity.ok(new MessageResponse("User registered successfully", 1));
    }

}
