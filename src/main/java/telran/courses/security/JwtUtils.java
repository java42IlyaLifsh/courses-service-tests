package telran.courses.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtils {
	@Value("${app.exiration.minutes: 60}")
	long expPeriodMin;
	@Value("{app.jwt.secret:x}")
	String secret;
	String create(String username) {
		Date currentDate = new Date();
		return Jwts.builder()
		.setExpiration(getExpDate(currentDate))
		.setIssuedAt(currentDate)
		.setSubject(username)
		.signWith(SignatureAlgorithm.HS512, secret)
		.compact();
	}
	private Date getExpDate(Date currentDate) {
		
		return new Date(currentDate.getTime() + expPeriodMin * 60000);
	}

}
