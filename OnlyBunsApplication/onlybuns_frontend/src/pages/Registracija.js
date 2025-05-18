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

export default function Registracija() {
  const [ime, setIme] = useState('');
  const [prezime, setPrezime] = useState('');
  const [ulica, setUlica] = useState('');
  const [grad, setGrad] = useState('');
  const [drzava, setDrzava] = useState('');
  const [broj, setBroj] = useState('');
  const [brojError, setBrojError] = useState(false);
  const [email, setEmail] = useState('');
  const [korisnickoIme, setKorisnickoIme] = useState('');
  const [g_duzina, setG_duzina] = useState('');
  const [g_sirina, setG_sirina] = useState('');
  const [emailError, setEmailError] = useState(false);
  const [korisnickoImeError, setKorisnickoImeError] = useState(false);
  const [UlicaError, setUlicaError] = useState(false);
  const [GradError, setGradError] = useState(false);
  const [DrzavaError, setDrzavaError] = useState(false);
  const [password, setPassword] = useState('');
  const [repeatPassword, setRepeatPassword] = useState('');
  const [passwordMismatch, setPasswordMismatch] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [uloga, setUloga] = useState('REGISTROVANI_KORISNIK');
  const navigate = useNavigate();
  const [successMessage, setSuccessMessage] = useState('');
  const isMounted = useRef(true);

  const validateBroj = (inputBroj) => {
    const brojRegex = /^\d{10}$/;
    return brojRegex.test(inputBroj);
  };

  const handleBrojChange = (e) => {
    const newBroj = e.target.value;
    setBroj(newBroj);
    setBrojError(!validateBroj(newBroj));
  };

  const validateEmail = (inputEmail) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(inputEmail);
  };

  const validateKorisnickoIme = (inputKorisnickoIme) => {
    const korisnickoImeRegex = /^[a-zA-Z0-9][a-zA-Z0-9._]{2,19}$/;
    return korisnickoImeRegex.test(inputKorisnickoIme);
  };

  const validateUlicaBroj = (inputUlicaBroj) => {
    const ulicaBrojRegex = /^[a-zA-Z0-9\s.,-]{5,100}$/;
    return ulicaBrojRegex.test(inputUlicaBroj);
  };

  const validateGrad = (inputGrad) => {
    const gradRegex = /^[a-zA-Z\s-]{2,50}$/;
    return gradRegex.test(inputGrad);
  };

  const validateDrzava = (inputDrzava) => {
    const drzavaRegex = /^[a-zA-Z\s-]{2,50}$/;
    return drzavaRegex.test(inputDrzava);
  };

  const handleEmailChange = (e) => {
    const newEmail = e.target.value;
    setEmail(newEmail);
    setEmailError(!validateEmail(newEmail));
  };

  const handleKorisnickoImeChange = (e) => {
    const newKorisnickoIme = e.target.value;
    setKorisnickoIme(newKorisnickoIme);
    setKorisnickoImeError(!validateKorisnickoIme(newKorisnickoIme));
  };

  const handleStreetAddressChange = (e) => {
    const newStreetAddress = e.target.value;
    setUlica(newStreetAddress);
    setUlicaError(!validateUlicaBroj(newStreetAddress));
  };

  const handleCityChange = (e) => {
    const newCity = e.target.value;
    setGrad(newCity);
    setGradError(!validateGrad(newCity));
  };

  const handleCountryChange = (e) => {
    const newCountry = e.target.value;
    setDrzava(newCountry);
    setDrzavaError(!validateDrzava(newCountry));
  };


  const checkEmailExists = async () => {
    try {
      const response = await fetch("http://localhost:8080/registrovaniKorisnik/emails");
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
      const response = await fetch(`http://localhost:8080/registrovaniKorisnik/check-username?username=${korisnickoIme}`);
      if (!response.ok) {
        console.error("Network response was not ok");
        return false; // Assume the username doesn't exist in case of network errors
      }
  
      const data = await response.json();
  
      // Check if the username exists based on the response

      //const exist = ;
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

    if (!validateBroj(broj)) {
      setErrorMessage('Enter valid phone number.');
      return;
    }

    if (!korisnickoIme || !ime || !prezime || !ulica || !grad || !drzava || !broj || !email || !password || !repeatPassword) {
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
      korisnickoIme,
      ime,
      prezime,
      broj,
      email,
      password,
      uloga,
      lokacija: {
        ulica,
        grad,
        drzava,
        g_sirina,
        g_duzina
      }
    };
    console.log(korisnik);

    setErrorMessage('');

    fetch("http://localhost:8080/registrovaniKorisnik/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(korisnik)
    }).then((response) => {
      if (response.ok) {
        console.log("Novi korisnik dodat");
        setSuccessMessage("Registration successful! Activation link is sent to your email address. Redirecting to login...");
  
        setTimeout(() => {
          if (isMounted.current) {
            setSuccessMessage("");
            navigate("/prijava");
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
          <Button component={Link} to="/prijava" color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
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
            <TextField fullWidth required label="First name" value={ime} onChange={(e) => setIme(e.target.value)} sx={{ mb: 1.5 }} />
            <TextField fullWidth required label="Last name" value={prezime} onChange={(e) => setPrezime(e.target.value)} sx={{ mb: 1.5 }} />
            <TextField fullWidth required label="Username" value={korisnickoIme} onChange={handleKorisnickoImeChange} error={korisnickoImeError} helperText={korisnickoImeError ? 'Enter valid username' : ''} sx={{ mb: 1.5 }} />
            <TextField fullWidth required label="Email" value={email} onChange={handleEmailChange} error={emailError} helperText={emailError ? 'Enter valid e-mail address' : ''} sx={{ mb: 1.5 }} />
            <TextField fullWidth required label="Password" type="password" value={password} onChange={(e) => setPassword(e.target.value)} sx={{ mb: 1.5 }} />
            <TextField fullWidth required label="Repeat password" type="password" value={repeatPassword} onChange={(e) => { setRepeatPassword(e.target.value); setPasswordMismatch(false); }} sx={{ mb: 1.5 }} />
            {passwordMismatch && <Typography color="error" variant="body2" gutterBottom>Passwords do not match.</Typography>}
            <TextField fullWidth required label="Street address" onChange={handleStreetAddressChange} error={UlicaError} helperText={UlicaError ? 'Enter valid street address' : ''} sx={{ mb: 1.5 }}/>
            <TextField fullWidth required label="City" onChange={handleCityChange} error={GradError} helperText={GradError ? 'Enter valid city name' : ''} sx={{ mb: 1.5 }}/>
            <TextField fullWidth required label="Country" onChange={handleCountryChange} error={DrzavaError} helperText={DrzavaError ? 'Enter valid country name' : ''} sx={{ mb: 1.5 }} />
            <TextField fullWidth required label="Phone number" value={broj} onChange={handleBrojChange} error={brojError} helperText={brojError ? 'Enter 10-digit phone number' : ''} sx={{ mb: 1.5 }} />
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
