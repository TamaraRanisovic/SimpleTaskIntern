import * as React from 'react';
import { useState, useRef, useEffect } from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { AppBar, Toolbar} from '@mui/material';
import { Link, useNavigate } from 'react-router-dom';
import logo from './photos/posticon.png';
const defaultTheme = createTheme();

export default function Registration() {
  const [name, setName] = useState('');
  const [surname, setSurname] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [phoneNumberError, setPhoneNumberError] = useState(false);
  const [email, setEmail] = useState('');
  const [username, setUsername] = useState('');
  const [emailError, setEmailError] = useState(false);
  const [usernameError, setUsernameError] = useState(false);
  const [password, setPassword] = useState('');
  const [repeatPassword, setRepeatPassword] = useState('');
  const [passwordMismatch, setPasswordMismatch] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [role, setRole] = useState('REGISTERED_USER');
  const navigate = useNavigate();
  const [successMessage, setSuccessMessage] = useState('');
  const isMounted = useRef(true);

  const validatePhoneNumber = (inputPhoneNumber) => {
    const phoneNumberRegex = /^\d{10}$/;
    return phoneNumberRegex.test(inputPhoneNumber);
  };

  const handlePhoneNumberChange = (e) => {
    const newPhoneNumber = e.target.value;
    setPhoneNumber(newPhoneNumber);
    setPhoneNumberError(!validatePhoneNumber(newPhoneNumber));
  };

  const validateEmail = (inputEmail) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(inputEmail);
  };

  const validateUsername = (inputUsername) => {
    const usernameRegex = /^[a-zA-Z0-9][a-zA-Z0-9._]{2,19}$/;
    return usernameRegex.test(inputUsername);
  };


  const handleEmailChange = (e) => {
    const newEmail = e.target.value;
    setEmail(newEmail);
    setEmailError(!validateEmail(newEmail));
  };

  const handleUsernameChange = (e) => {
    const newUsername = e.target.value;
    setUsername(newUsername);
    setUsernameError(!validateUsername(newUsername));
  };


  const checkEmailExists = async () => {
    try {
      const response = await fetch("http://localhost:8080/registeredUser/emails");
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      const data = await response.json();
      return data.includes(email);
    } catch (error) {
      console.error("Error checking email:", error);
      return false;
    }
  };


  const checkUsernameExists = async () => {
    try {
      const response = await fetch(`http://localhost:8080/registeredUser/check-username?username=${username}`);
      if (!response.ok) {
        console.error("Network response was not ok");
        return false; // Assume the username doesn't exist in case of network errors
      }
  
      const data = await response.json();

      return data.includes("true"); // Username doesn't exist, return message
    } catch (error) {
      console.error("Error checking username:", error);
      return false;
    }
  };
  
   useEffect(() => {
      isMounted.current = true;
      return () => {
        isMounted.current = false;
      };
    }, []);
  

  const handleSubmit = async (event) => {
    event.preventDefault();
    if (password !== repeatPassword) {
      setPasswordMismatch(true);
      return;
    }

    if (!validatePhoneNumber(phoneNumber)) {
      setErrorMessage('Enter valid phone number.');
      return;
    }

    if (!username || !name || !surname || !phoneNumber || !email || !password || !repeatPassword) {
      setErrorMessage('Enter valid data.');
      return;
    }

    const emailExists = await checkEmailExists();
    if (emailExists) {
      setErrorMessage('Email address already exists.');
      return;
    }

    const usernameExists = await checkUsernameExists();
    if (usernameExists) {
      setErrorMessage('Username already exists.');
      return;
    }

    const korisnik = {
      username,
      name,
      surname,
      phoneNumber,
      email,
      password,
      role
    };
    console.log(korisnik);

    setErrorMessage('');

    fetch("http://localhost:8080/registeredUser/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(korisnik)
    }).then((response) => {
      if (response.ok) {
        console.log("Novi korisnik dodat");
        setSuccessMessage("Registration successful! Activation link is sent to your email address. Redirecting to login...");
        setErrorMessage("");

        setTimeout(() => {
          if (isMounted.current) {
            setSuccessMessage("");
            navigate("/login");
          }
        }, 15000);
      } else if (response.status === 400) {
        console.log("Enter valid location.");
        setErrorMessage("Enter valid location.");
      } else {
        console.log("An unexpected error occurred. Please try again.");
        setErrorMessage("An unexpected error occurred. Please try again.");
      }
    });
  };

  return (
      
    <ThemeProvider theme={defaultTheme}>
      {/* Navigation Bar */}
      <AppBar position="static" sx={{ bgcolor: '#b4a7d6' }}>
      <Toolbar sx={{ justifyContent: 'space-between' }}>
        <Link to="/" style={{ textDecoration: 'none', display: 'flex', alignItems: 'center', color: 'inherit' }}>
          <Box sx={{ display: 'flex', alignItems: 'center', ml: 2 }}>
            <img src={logo} alt="OnlyBuns Logo" style={{ height: '40px', marginRight: '10px' }} />
            <Typography variant="h5" component="div" sx={{ fontWeight: 'bold' }}>
              OnlyBuns
            </Typography>
          </Box>
        </Link>
        <Box sx={{ display: 'flex', gap: 2, mr: 2 }}>
          <Button component={Link} to="/login" color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
            Log In
          </Button>
        </Box>
      </Toolbar>
    </AppBar>
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <Box sx={{ marginTop: 4, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
          <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5" sx={{ marginBottom: 2}}>
            Registration
          </Typography>
          <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
            <TextField fullWidth required label="First name" value={name} onChange={(e) => setName(e.target.value)} sx={{ mb: 1.5 }} />
            <TextField fullWidth required label="Last name" value={surname} onChange={(e) => setSurname(e.target.value)} sx={{ mb: 1.5 }} />
            <TextField fullWidth required label="Username" value={username} onChange={handleUsernameChange} error={usernameError} helperText={usernameError ? 'Enter valid username' : ''} sx={{ mb: 1.5 }} />
            <TextField fullWidth required label="Email" value={email} onChange={handleEmailChange} error={emailError} helperText={emailError ? 'Enter valid e-mail address' : ''} sx={{ mb: 1.5 }} />
            <TextField fullWidth required label="Password" type="password" value={password} onChange={(e) => setPassword(e.target.value)} sx={{ mb: 1.5 }} />
            <TextField fullWidth required label="Repeat password" type="password" value={repeatPassword} onChange={(e) => { setRepeatPassword(e.target.value); setPasswordMismatch(false); }} sx={{ mb: 1.5 }} />
            {passwordMismatch && <Typography color="error" variant="body2" gutterBottom>Passwords do not match.</Typography>}
            <TextField fullWidth required label="Phone number" value={phoneNumber} onChange={handlePhoneNumberChange} error={phoneNumberError} helperText={phoneNumberError ? 'Enter 10-digit phone number' : ''} sx={{ mb: 1.5 }} />
            <Button type="submit" sx={{ padding: '5px 10px', borderRadius: '15px', fontSize: '1rem', fontWeight: 'bold', mt: 2, mb: 3 }} fullWidth variant="contained"  color="secondary">
              Sign in
            </Button>
            {errorMessage && <Typography color="error" sx={{ mb: 3}} variant="body2" gutterBottom >{errorMessage}</Typography>}
            {successMessage && (
              <Typography color="green" sx={{ mb: 4 }} variant="body2" gutterBottom>
                  {successMessage}
              </Typography>
            )}
          </Box>
        </Box>
      </Container>
    </ThemeProvider>
  );
}
